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

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UsersComponent implements OnInit {
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);
  
  users: User[] = [];
  
  newUser = {
    firstName: '',
    lastName: '',
    email: ''
  };

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.http.get<User[]>('http://localhost:8080/api/users')
      .subscribe({
        next: (data) => { 
          this.users = data; 
          this.cdr.markForCheck(); 
        },
        error: (err) => console.error('Error fetching users', err)
      });
  }

  addUser() {
    if (!this.newUser.firstName || !this.newUser.lastName || !this.newUser.email) {
      alert('Wypełnij wszystkie pola!');
      return;
    }

    this.http.post('http://localhost:8080/api/users', this.newUser)
      .subscribe({
        next: () => {
          alert('Użytkownik dodany!');
          this.newUser = { firstName: '', lastName: '', email: '' };
          this.loadUsers();
        },
        error: (err) => {
          console.error('Error adding user', err);
          
          if (err.status === 409) {
            alert(err.error);
          } else {
            alert('Błąd dodawania użytkownika: ' + (err.error || err.message));
          }
        }
      });
  }

  deleteUser(id: number) {
    if (!confirm('Czy na pewno chcesz usunąć tego użytkownika?')) {
      return;
    }

    this.http.delete(`http://localhost:8080/api/users/${id}`)
      .subscribe({
        next: () => {
          alert('Użytkownik usunięty!');
          this.loadUsers();
        },
        error: (err) => {
          console.error('Error deleting user', err);
          
          if (err.status === 409) {
            alert(err.error);
          } else {
            alert('Błąd usuwania użytkownika!');
          }
        }
      });
  }
}
