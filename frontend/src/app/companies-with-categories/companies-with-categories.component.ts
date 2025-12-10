import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';

interface CompanyWithCategory {
  id: number;
  name: string;
  symbol: string;
  categoryName: string;
  description: string;
}

@Component({
  selector: 'app-companies-with-categories',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './companies-with-categories.component.html',
  styleUrl: './companies-with-categories.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CompaniesWithCategoriesComponent implements OnInit {
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);
  
  companiesWithCategory: CompanyWithCategory[] = [];

  ngOnInit() {
    this.http.get<CompanyWithCategory[]>('http://localhost:8080/api/companies/with-category')
      .subscribe({
        next: (data) => { 
          this.companiesWithCategory = data; 
          this.cdr.markForCheck(); 
        },
        error: (err) => console.error('Error fetching companies with category', err)
      });
  }
}