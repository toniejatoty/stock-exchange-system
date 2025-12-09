import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { FormsModule } from '@angular/forms'; 

interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
}

interface Company {
  id: number;
  name: string;
  symbol: string;
  categoryId: number;
}

interface Portfolio {
  id: number;
  userId: number;
  companyId: number;
  quantity: number;
  lastUpdated: string;
}
interface CompanyWithCategory {
  id: number;
  name: string;
  symbol: string;
  categoryName: string;
}


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class App implements OnInit {
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);
  
  companies: Company[] = [];
  users: User[] = [];
  portfolios: Portfolio[] = [];
  companiesWithCategory: CompanyWithCategory[] = [];
  isDarkMode = true;  // domyślnie dark mode
  newPortfolio: Portfolio = {
    id: 0,
    userId: 0,
    companyId: 0,
    quantity: 0,
    lastUpdated: ''
  };

  ngOnInit() {
    this.loadTheme();
    this.http.get<Company[]>('http://localhost:8080/api/companies')
      .subscribe({
        next: (data) => {
          console.log('Companies loaded:', data);
          this.companies = data;
          this.cdr.markForCheck();
        },
        error: (error) => console.error('Error fetching companies:', error)
      });
    
    // Pobierz użytkowników
    this.http.get<User[]>('http://localhost:8080/api/users')
      .subscribe({
        next: (data) => {
          console.log('Users loaded:', data);
          this.users = data;
          this.cdr.markForCheck();
        },
        error: (error) => console.error('Error fetching users:', error)
      });
    
    // Pobierz portfolios
    this.http.get<Portfolio[]>('http://localhost:8080/api/portfolios')
      .subscribe({
        next: (data) => {
          console.log('Portfolios loaded:', data);
          this.portfolios = data;
          this.cdr.markForCheck();
        },
        error: (error) => console.error('Error fetching portfolios:', error)
      });

      this.http.get<CompanyWithCategory[]>('http://localhost:8080/api/companies/with-category')
  .subscribe({
    next: (data) => { this.companiesWithCategory = data; this.cdr.markForCheck(); },
    error: (err) => console.error('Error fetching companies with category', err)
  });
  }

  toggleDarkMode() {
    this.isDarkMode = !this.isDarkMode;
    localStorage.setItem('darkMode', this.isDarkMode.toString());
    this.applyTheme();
    this.cdr.markForCheck();
  }
loadTheme() {
  const saved = localStorage.getItem('darkMode');
  if (saved !== null) {
    this.isDarkMode = saved === 'true';  // konwertuje string na boolean
  }
  this.applyTheme();
}
    applyTheme() {
    const body = document.body;
    if (this.isDarkMode) {
      body.classList.add('dark-mode');
      body.classList.remove('light-mode');
    } else {
      body.classList.add('light-mode');
      body.classList.remove('dark-mode');
    }
  }


  addPortfolio() {
    if (!this.newPortfolio.userId || !this.newPortfolio.companyId || !this.newPortfolio.quantity) {
      alert('Wypełnij wszystkie pola!');
      return;
    }
  const payload = {
    userId: this.newPortfolio.userId,
    companyId: this.newPortfolio.companyId,
    quantity: this.newPortfolio.quantity
  };
    
    this.http.post<Portfolio>('http://localhost:8080/api/portfolios', payload)
      .subscribe({
        next: (data) => {
          console.log('Portfolio added:', data);
          this.portfolios.push(data);
          this.newPortfolio = { id: 0, userId: 0, companyId: 0, quantity: 0, lastUpdated: '' };
          this.cdr.markForCheck();
          alert('Portfolio dodane!');
        },
        error: (error) => console.error('Error adding portfolio:', error)
      });
  }
}