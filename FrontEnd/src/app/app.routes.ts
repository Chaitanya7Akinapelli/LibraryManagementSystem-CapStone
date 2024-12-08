import { Routes } from '@angular/router';
import { RegistrationComponent } from './Components/registration/registration.component';
import { HomeComponent } from './Components/home/home.component';
import { LoginComponent } from './Components/login/login.component';
import { MainPageComponent } from './Components/main-page/main-page.component';
import { UserPageComponent } from './Components/user-page/user-page.component';
import { AddBookComponent } from './Components/add-book/add-book.component';
import { DeleteBookComponent } from './Components/delete-book/delete-book.component';
import { SearchBookComponent } from './Components/search-book/search-book.component';
import { BooksReturningByadminComponent } from './Components/books-returning-byadmin/books-returning-byadmin.component';
import { ReportsComponent } from './Components/reports/reports.component';
import { BorrowComponent } from './Components/borrow/borrow.component';
import { BorrowingHistoryComponent } from './Components/borrowing-history/borrowing-history.component';

export const routes: Routes = [
    {path : '' , component : HomeComponent},
    {path : 'register' , component : RegistrationComponent},
    {path : 'login' , component : LoginComponent},
    {path : 'main' , component : MainPageComponent},
    {path : 'user' , component : UserPageComponent},
    {path : 'addbooks' , component : AddBookComponent},
    {path : 'deletebooks' , component : DeleteBookComponent},
    {path : 'searchbooks' , component : SearchBookComponent},
    {path : 'returnbooks' , component : BooksReturningByadminComponent},
    {path : 'reports' , component : ReportsComponent},
    {path : 'borrowbooks' , component : BorrowComponent},
    {path : 'borrowinghistory' , component : BorrowingHistoryComponent}
];
