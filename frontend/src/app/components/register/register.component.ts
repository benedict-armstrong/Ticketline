import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { Address } from "src/app/dtos/address";
import { User } from "../../dtos/user";
import { ApplicationUserService } from "../../services/user.service";

@Component({
  selector: "app-register",
  templateUrl: "./register.component.html",
  styleUrls: ["./register.component.scss"],
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = "";

  // Success Flag
  success = false;

  constructor(
    private applicationUserService: ApplicationUserService,
    private formBuilder: FormBuilder,
    private router: Router
  ) {
    this.registerForm = this.formBuilder.group({
      firstName: ["", [Validators.required]],
      lastName: ["", [Validators.required]],
      telephoneNumber: [""],
      email: ["", [Validators.required]],
      password: ["", [Validators.required, Validators.minLength(8)]],
      passwordRepeat: ["", [Validators.required, Validators.minLength(8)]],
      points: [0, [Validators.min(0)]],
      status: ["ACTIVE", [Validators.required]],
      role: ["CLIENT", [Validators.required]],
      addressName: ["", [Validators.required]],
      lineOne: ["", [Validators.required]],
      lineTwo: [""],
      city: ["", [Validators.required]],
      postcode: ["", [Validators.required]],
      country: ["", [Validators.required]],
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  addUser() {
    this.submitted = true;
    if (this.registerForm.valid) {
      var user: User = new User(
        null,
        this.registerForm.value.firstName,
        this.registerForm.value.lastName,
        this.registerForm.value.telephoneNumber,
        this.registerForm.value.email,
        this.registerForm.value.password,
        new Date(),
        this.registerForm.value.points,
        "ACTIVE",
        "CLIENT",
        new Address(
          null,
          this.registerForm.value.addressName,
          this.registerForm.value.lineOne,
          this.registerForm.value.lineTwo,
          this.registerForm.value.city,
          this.registerForm.value.postcode,
          this.registerForm.value.country
        )
      );
      this.applicationUserService.createUser(user).subscribe(
        () => {
          this.success = true;
        },
        (error) => {
          this.defaultServiceErrorHandling(error);
        }
      );
    } else {
      console.log("Invalid input");
    }
  }

  vanishAlert(): void {
    this.error = false;
    this.success = false;
  }

  ngOnInit(): void {}

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === "object") {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }
}
