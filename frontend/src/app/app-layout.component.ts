import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { inject } from '@angular/core';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, CommonModule],
  template: `
    <nav class="navbar">
      <a routerLink="/" class="nav-link">Home</a>
      <a routerLink="/portfolios" class="nav-link">Portfolios</a>
      <a routerLink="/orders" class="nav-link">Zamówienia</a>
      <a routerLink="/companies-categories" class="nav-link">Spółki z kategoriami</a>
      <a routerLink="/users" class="nav-link">Użytkownicy</a>
    </nav>
    <div class="theme-toggle">
      <button (click)="toggleDarkMode()" class="toggle-btn">
        {{ isDarkMode ? '☀️ Light Mode' : '🌙 Dark Mode' }}
      </button>
    </div>
    <router-outlet />
  `,
  styleUrl: './app.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppLayoutComponent implements OnInit {
  private cdr = inject(ChangeDetectorRef);
  isDarkMode: boolean = true;

  ngOnInit() {
    this.loadTheme();
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
      this.isDarkMode = saved === 'true';
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
}