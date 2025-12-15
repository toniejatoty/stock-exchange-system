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



@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule],
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
  newPortfolio: Portfolio = {
    id: 0,
    userId: 0,
    companyId: 0,
    quantity: 0,
    lastUpdated: ''
  };

  ngOnInit() {
    
    
    // Pobierz użytkowników

    
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