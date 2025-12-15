import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

interface CompanyWithCategory {
  id: number;
  name: string;
  symbol: string;
  categoryName: string;
  description: string;
}
interface Category {
  id: number;
  name: string;
  description: string;
}

@Component({
  selector: 'app-companies-with-categories',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './companies-with-categories.component.html',
  styleUrl: './companies-with-categories.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CompaniesWithCategoriesComponent implements OnInit {
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);
  
  companiesWithCategory: CompanyWithCategory[] = [];
  filteredCompanies: CompanyWithCategory[] = [];
  categories: Category[] = [];
  selectedCategoryId: number = 0;
  newCompany = {
    name: '',
    symbol: '',
    categoryId: 0
  };


  
  ngOnInit() {
    this.loadCompanies();
    this.loadCategories();
  }

  filterCompanies() {
    if (Number(this.selectedCategoryId) === 0) {
      this.filteredCompanies = this.companiesWithCategory;
    } else {
      this.filteredCompanies = this.companiesWithCategory.filter(
        company => company.categoryName === this.categories.find(c => c.id === Number(this.selectedCategoryId))?.name
      );
    }
    this.cdr.markForCheck();
  }

   loadCompanies() {
    this.http.get<CompanyWithCategory[]>('http://localhost:8080/api/companies/with-category')
      .subscribe({
        next: (data) => { 
          this.companiesWithCategory = data;
          this.filterCompanies();
          this.cdr.markForCheck();
        },
        error: (err) => console.error('Error fetching companies with category', err)
      });
  }
    loadCategories() {
    this.http.get<Category[]>('http://localhost:8080/api/categories')
      .subscribe({
        next: (data) => { 
          this.categories = data; 
          this.cdr.markForCheck(); 
        },
        error: (err) => console.error('Error fetching categories', err)
      });
  }

  addCompany() {
    if (!this.newCompany.name || !this.newCompany.symbol || !this.newCompany.categoryId) {
      alert('Wypełnij wszystkie pola!');
      return;
    }

    this.http.post('http://localhost:8080/api/companies', this.newCompany)
      .subscribe({
        next: () => {
          alert('Spółka dodana!');
          this.newCompany = { name: '', symbol: '', categoryId: 0 };
          this.loadCompanies();
        },
        error: (err) => {
  console.error('Error adding company', err);
  
  // Pokaż dokładny komunikat z backendu
  if (err.status === 409) {
    alert(err.error);  // Pokaże: "Spółka z tickerem 'CDR' lub nazwą 'CD Projekt Red' już istnieje!"
  } else {
    alert('Błąd dodawania spółki: ' + (err.error || err.message));
  }
}
      });
  }
deleteCompany(id: number) {
    if (!confirm('Czy na pewno chcesz usunąć tę spółkę?')) {
      return;
    }

    this.http.delete(`http://localhost:8080/api/companies/${id}`)
      .subscribe({
        next: () => {
          alert('Spółka usunięta!');
          this.loadCompanies();
        },
        error: (err) => {
  console.error('Error deleting company', err);
  
  // Pokaż dokładny komunikat z backendu
  if (err.status === 409) {
    alert(err.error);  // Pokaże: "Nie można usunąć spółki - jest używana w X portfolio(s)"
  } else {
    alert('Błąd usuwania spółki: ' + (err.error || err.message));
  }
}
      });
  }



}