import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

interface Category {
  id: number;
  name: string;
  description: string;
}

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CategoriesComponent implements OnInit {
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);
  
  categories: Category[] = [];
  
  newCategory = {
    name: '',
    description: ''
  };

  ngOnInit() {
    this.loadCategories();
  }

  loadCategories() {
    this.http.get<Category[]>('http://localhost:8080/api/categories')
      .subscribe({
        next: (data) => {
          this.categories = data;
          this.cdr.markForCheck();
        },
        error: (error) => console.error('Error fetching categories:', error)
      });
  }

  addCategory() {
    if (!this.newCategory.name || !this.newCategory.description) {
      alert('Wypełnij wszystkie pola!');
      return;
    }

    this.http.post<Category>('http://localhost:8080/api/categories', this.newCategory)
      .subscribe({
        next: () => {
          alert('Kategoria dodana!');
          this.newCategory = { name: '', description: '' };
          this.loadCategories();
        },
        error: (error) => {
          console.error('Error adding category:', error);
          alert('Błąd dodawania kategorii: ' + (error.error || 'Nieznany błąd'));
        }
      });
  }

  deleteCategory(id: number) {
    if (!confirm('Czy na pewno chcesz usunąć tę kategorię?')) {
      return;
    }

    this.http.delete(`http://localhost:8080/api/categories/${id}`, { responseType: 'text' })
      .subscribe({
        next: () => {
          alert('Kategoria usunięta!');
          this.loadCategories();
        },
        error: (err) => {
          console.error('Error deleting category', err);
          alert('Błąd: ' + (err.error || 'Nie można usunąć kategorii'));
        }
      });
  }
}
