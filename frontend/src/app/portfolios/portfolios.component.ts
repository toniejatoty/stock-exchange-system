import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
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
  selector: 'app-portfolios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './portfolios.component.html',
  styleUrl: './portfolios.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PortfoliosComponent implements OnInit {
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);
  
  companies: Company[] = [];
  users: User[] = [];
  portfolios: Portfolio[] = [];
  filteredPortfolios: Portfolio[] = [];
  selectedUserId: number = 0;
  
  newPortfolio = {
    userId: 0,
    companyId: 0,
    quantity: 0
  };

  ngOnInit() {
    this.loadPortfolios();
    this.loadUsers();
    this.loadCompanies();
  }

  loadPortfolios() {
    this.http.get<Portfolio[]>('http://localhost:8080/api/portfolios')
      .subscribe({
        next: (data) => {
          this.portfolios = data;
          this.filterPortfolios();
          this.cdr.markForCheck();
        },
        error: (error) => console.error('Error fetching portfolios:', error)
      });
  }

  loadUsers() {
    this.http.get<User[]>('http://localhost:8080/api/users')
      .subscribe({
        next: (data) => {
          this.users = data;
          this.cdr.markForCheck();
        },
        error: (error) => console.error('Error fetching users:', error)
      });
  }

  loadCompanies() {
    this.http.get<Company[]>('http://localhost:8080/api/companies')
      .subscribe({
        next: (data) => {
          this.companies = data;
          this.cdr.markForCheck();
        },
        error: (error) => console.error('Error fetching companies:', error)
      });
  }

  addPortfolio() {
    if (!this.newPortfolio.userId || !this.newPortfolio.companyId || !this.newPortfolio.quantity) {
      alert('Wypełnij wszystkie pola!');
      return;
    }

    this.http.post<Portfolio>('http://localhost:8080/api/portfolios', this.newPortfolio)
      .subscribe({
        next: () => {
          alert('Portfolio dodane!');
          this.newPortfolio = { userId: 0, companyId: 0, quantity: 0 };
          this.loadPortfolios();
        },
        error: (error) => {
          console.error('Error adding portfolio:', error);
          alert('Błąd dodawania portfolio!');
        }
      });
  }

  deletePortfolio(id: number) {
    if (!confirm('Czy na pewno chcesz usunąć to portfolio?')) {
      return;
    }

    this.http.delete(`http://localhost:8080/api/portfolios/${id}`)
      .subscribe({
        next: () => {
          alert('Portfolio usunięte!');
          this.loadPortfolios();
        },
        error: (err) => {
          console.error('Error deleting portfolio', err);
          alert('Błąd usuwania portfolio!');
        }
      });
  }

  filterPortfolios(): void {
    if (this.selectedUserId === 0 || this.selectedUserId === '0' as any) {
      this.filteredPortfolios = [...this.portfolios];
    } else {
      const userId = Number(this.selectedUserId);
      this.filteredPortfolios = this.portfolios.filter(
        p => p.userId === userId
      );
    }
  }

  onUserFilterChange(): void {
    this.filterPortfolios();
    this.cdr.markForCheck();
  }

  getUserName(userId: number): string {
    const user = this.users.find(u => u.id === userId);
    return user ? `${user.firstName} ${user.lastName}` : `ID: ${userId}`;
  }

  getCompanyName(companyId: number): string {
    const company = this.companies.find(c => c.id === companyId);
    return company ? company.name : `ID: ${companyId}`;
  }
}
