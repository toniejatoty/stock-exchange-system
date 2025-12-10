import { Routes } from '@angular/router';
import { App } from './app';
import { CompaniesWithCategoriesComponent } from './companies-with-categories/companies-with-categories.component';

export const routes: Routes = [
  { path: '', component: App },
  { path: 'companies-categories', component: CompaniesWithCategoriesComponent }
];