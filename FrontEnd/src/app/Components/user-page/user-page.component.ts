import { Component } from '@angular/core';
import { UserNavBarComponent } from "../user-nav-bar/user-nav-bar.component";

@Component({
  selector: 'app-user-page',
  standalone: true,
  imports: [UserNavBarComponent],
  templateUrl: './user-page.component.html',
  styleUrl: './user-page.component.css'
})
export class UserPageComponent {

}
