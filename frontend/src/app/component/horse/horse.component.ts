import { Component, OnInit } from "@angular/core";
import { Horse } from "../../dto/horse";
import { HorseService } from "src/app/service/horse.service";
import { UserNotificationService } from "src/app/service/user-notification.service";

@Component({
  selector: "app-horse",
  templateUrl: "./horse.component.html",
  styleUrls: ["./horse.component.scss"],
})
export class HorseComponent implements OnInit {
  search = false;
  horses: Horse[];

  constructor(
    private service: HorseService,
    private userNotificationService: UserNotificationService
  ) {}

  ngOnInit(): void {
    this.reloadHorses();
  }

  reloadHorses() {
    this.service.getAllHorses().subscribe({
      next: (data) => {
        console.log("received horses", data);
        this.horses = data;
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
