import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  companies: string[] = [];
  users: string[] = [];
  
  ngOnInit() {
    // Pobierz spółki
    fetch('http://localhost:8080/api/companies')
      .then(res => res.json())
      .then(data => this.companies = data)
      .catch(error => console.error('Error fetching companies:', error));
    
    // Pobierz użytkowników
    fetch('http://localhost:8080/api/users/fullnames')
      .then(res => res.json())
      .then(data => this.users = data)
      .catch(error => console.error('Error fetching users:', error));
  }
}