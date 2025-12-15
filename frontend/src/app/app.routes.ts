import { Routes } from '@angular/router';
import { App } from './app';
import { CompaniesWithCategoriesComponent } from './companies-with-categories/companies-with-categories.component';
import { UsersComponent } from './users/users.component';
import { PortfoliosComponent } from './portfolios/portfolios.component';
import { OrdersComponent } from './orders/orders.component';

export const routes: Routes = [
  { path: '', component: App },
  { path: 'portfolios', component: PortfoliosComponent },
  { path: 'orders', component: OrdersComponent },
  { path: 'companies-categories', component: CompaniesWithCategoriesComponent },
  { path: 'users', component: UsersComponent }
];