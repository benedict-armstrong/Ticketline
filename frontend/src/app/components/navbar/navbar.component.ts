import { Component, OnInit } from "@angular/core";
import { AuthService } from "src/app/services/auth.service";

@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.scss"],
})
export class NavbarComponent implements OnInit {
  constructor(private authService: AuthService) {}

  ngOnInit(): void {}

  /**
   * Logout current user.
   */
  logoutUser(): void {
    this.authService.logoutUser();
  }

  /**
   * Checks if user is logged in.
   */
  isLoggedIn() {
    return this.authService.isLoggedIn();
  }

  hasAdminPermission(): boolean {
    return this.authService.getUserRole() === "ADMIN";
  }
}
