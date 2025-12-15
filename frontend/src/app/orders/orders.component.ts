import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent implements OnInit {
  orders: any[] = [];
  filteredOrders: any[] = [];
  users: any[] = [];
  companies: any[] = [];
  
  selectedFilter: string = 'ALL';
  selectedUserId: number = 0;
  selectedCompanyId: number = 0;
  selectedOrderType: string = 'ALL';
  startDate: string = '';
  endDate: string = '';
  
  newOrder = {
    userId: 0,
    companyId: 0,
    orderType: 'BUY',
    quantity: 0,
    price: 0,
    status: 'PENDING'
  };

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadOrders();
    this.loadUsers();
    this.loadCompanies();
  }

  loadOrders(): void {
    this.http.get<any[]>('http://localhost:8080/api/orders')
      .subscribe({
        next: (data) => {
          this.orders = data;
          this.applyFilter();
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Błąd ładowania zamówień:', err);
          alert('Błąd ładowania zamówień');
        }
      });
  }

  loadUsers(): void {
    this.http.get<any[]>('http://localhost:8080/api/users')
      .subscribe({
        next: (data) => {
          this.users = data;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Błąd ładowania użytkowników:', err);
        }
      });
  }

  loadCompanies(): void {
    this.http.get<any[]>('http://localhost:8080/api/companies')
      .subscribe({
        next: (data) => {
          this.companies = data;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Błąd ładowania firm:', err);
        }
      });
  }

  applyFilter(): void {
    this.filteredOrders = this.orders.filter(order => {
      // Filter by status
      if (this.selectedFilter !== 'ALL' && order.status !== this.selectedFilter) {
        return false;
      }
      
      // Filter by user
      if (this.selectedUserId != 0 && order.userId != this.selectedUserId) {
        return false;
      }
      
      // Filter by company
      if (this.selectedCompanyId != 0 && order.companyId != this.selectedCompanyId) {
        return false;
      }
      
      // Filter by order type
      if (this.selectedOrderType !== 'ALL' && order.orderType !== this.selectedOrderType) {
        return false;
      }
      
      // Filter by date range
      if (this.startDate) {
        const orderDate = new Date(order.orderDate);
        const startDateTime = new Date(this.startDate);
        if (orderDate < startDateTime) {
          return false;
        }
      }
      
      if (this.endDate) {
        const orderDate = new Date(order.orderDate);
        const endDateTime = new Date(this.endDate);
        endDateTime.setHours(23, 59, 59, 999); // End of day
        if (orderDate > endDateTime) {
          return false;
        }
      }
      
      return true;
    });
    this.cdr.detectChanges();
  }

  onFilterChange(): void {
    this.applyFilter();
  }

  clearFilters(): void {
    this.selectedFilter = 'ALL';
    this.selectedUserId = 0;
    this.selectedCompanyId = 0;
    this.selectedOrderType = 'ALL';
    this.startDate = '';
    this.endDate = '';
    this.applyFilter();
  }

  addOrder(): void {
    if (this.newOrder.userId === 0 || this.newOrder.companyId === 0) {
      alert('Proszę wybrać użytkownika i firmę');
      return;
    }
    
    if (this.newOrder.quantity <= 0 || this.newOrder.price <= 0) {
      alert('Ilość i cena muszą być większe od 0');
      return;
    }

    
    this.http.post('http://localhost:8080/api/orders', this.newOrder)
      .subscribe({
        next: () => {
          this.loadOrders();
          this.newOrder = {
            userId: 0,
            companyId: 0,
            orderType: 'BUY',
            quantity: 0,
            price: 0,
            status: 'PENDING'
          };
        },
        error: (err) => {
          if (err.status === 400) {
            alert('Błąd walidacji: ' + (err.error || 'Nieprawidłowe dane'));
          } else {
            alert('Błąd podczas dodawania zamówienia');
          }
        }
      });
  }

  cancelOrder(id: number): void {
    if (confirm('Czy na pewno chcesz anulować to zamówienie?')) {
      this.http.put(`http://localhost:8080/api/orders/${id}/cancel`, {})
        .subscribe({
          next: () => {
            this.loadOrders();
          },
          error: (err) => {
            if (err.status === 400) {
              alert('Błąd: ' + (err.error || 'Nie można anulować zamówienia'));
            } else {
              alert('Błąd podczas anulowania zamówienia');
            }
          }
        });
    }
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
