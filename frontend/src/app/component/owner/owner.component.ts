import { Component, OnInit } from "@angular/core";
import { Owner } from "src/app/dto/owner";
import { OwnerService } from "src/app/service/owner.service";
import { UserNotificationService } from "src/app/service/user-notification.service";

@Component({
  selector: "app-owner",
  templateUrl: "./owner.component.html",
  styleUrls: ["./owner.component.scss"],
})
export class OwnerComponent implements OnInit {
  // search = false;
  owners: Owner[];
  // error: string = null;

  constructor(
    private service: OwnerService,
    private userNotificationService: UserNotificationService
  ) {}

  ngOnInit(): void {
    this.reloadOwners();
  }

  reloadOwners() {
    this.service.getAllOwners().subscribe({
      next: (owners) => {
        this.owners = owners;
      },
      error: (error) => {
        this.userNotificationService.addNotification({
          message: error.error.message,
          type: "error",
        });
      },
    });
  }
}
