import { Component, Input, OnInit, Output } from "@angular/core";
import { Observable, Subscription } from "rxjs";
import { UserNotificationService } from "src/app/service/user-notification.service";
import { UserNotification } from "src/app/types/responseMessage";

@Component({
  selector: "app-user-notification",
  templateUrl: "./user-notification.component.html",
  styleUrls: ["./user-notification.component.scss"],
})
export class UserNotificationComponent implements OnInit {
  @Input()
  messages: Observable<UserNotification>;

  userMessage: UserNotification = { message: null, type: null };

  alertType = "alert-success";

  hidden = true;
  hiddenTimeOutId = 0;

  constructor(private userNotificationService: UserNotificationService) {}

  ngOnInit(): void {
    this.userNotificationService.getNotifications().subscribe((message_) => {
      this.userMessage = message_;
      switch (message_.type) {
        case "error":
          this.alertType = "alert-failure";
          break;
        case "warning":
          this.alertType = "alert-warning";
          break;
        case "success":
          this.alertType = "alert-success";
          break;
      }

      // if multiple notification in rapid succession, blink in case same notification
      this.hidden = true;
      setTimeout(() => {
        this.hidden = false;
      }, 50);

      // reset timeout
      clearTimeout(this.hiddenTimeOutId);
      this.hiddenTimeOutId = setTimeout(() => {
        this.hidden = true;
      }, 5000);
    });
  }
}
