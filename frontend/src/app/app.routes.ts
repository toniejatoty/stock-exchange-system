import { Routes } from '@angular/router';
import { App } from './app';
import { CompaniesWithCategoriesComponent } from './companies-with-categories/companies-with-categories.component';
import { UsersComponent } from './users/users.component';
import { PortfoliosComponent } from './portfolios/portfolios.component';

export const routes: Routes = [
  { path: '', component: App },
  { path: 'companies-categories', component: CompaniesWithCategoriesComponent },
  { path: 'users', component: UsersComponent },
  { path: 'portfolios', component: PortfoliosComponent }
];