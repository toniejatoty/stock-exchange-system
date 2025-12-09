import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';

interface Portfolio {
  id: number;
  userId: number;
  companyId: number;
  quantity: number;
  lastUpdated: string;
}

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
  changeDetection: ChangeDetectionStrategy.OnPush  // update view when change
})
export class App implements OnInit {
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);  // notify when change
  
  companies: string[] = [];
  users: string[] = [];
  portfolios: Portfolio[] = [];
  
  ngOnInit() {
    // Pobierz spółki
    this.http.get<string[]>('http://localhost:8080/api/companies')
      .subscribe({
        next: (data) => {
          console.log('Companies loaded:', data);
          this.companies = data;
          this.cdr.markForCheck();  // notify that there is change
        },
        error: (error) => console.error('Error fetching companies:', error)
      });
    
    // Pobierz użytkowników
    this.http.get<string[]>('http://localhost:8080/api/users/fullnames')
      .subscribe({
        next: (data) => {
          console.log('Users loaded:', data);
          this.users = data;
          this.cdr.markForCheck();  // notify that there is change
        },
        error: (error) => console.error('Error fetching users:', error)
      });
    
    // Pobierz portfolios
    this.http.get<Portfolio[]>('http://localhost:8080/api/portfolios')
      .subscribe({
        next: (data) => {
          console.log('Portfolios loaded:', data);
          this.portfolios = data;
          this.cdr.markForCheck();  // notify that there is change
        },
        error: (error) => console.error('Error fetching portfolios:', error)
      });
  }
}