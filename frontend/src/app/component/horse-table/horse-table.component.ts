import { Component, Input, OnInit } from "@angular/core";
import { Horse } from "src/app/dto/horse";
import { HorseService } from "src/app/service/horse.service";
import { UserNotificationService } from "src/app/service/user-notification.service";

@Component({
  selector: "app-horse-table",
  templateUrl: "./horse-table.component.html",
  styleUrls: ["./horse-table.component.scss"],
})
export class HorseTableComponent implements OnInit {
  @Input()
  horses: Horse[];

  constructor(
    private service: HorseService,
    private userNotificationService: UserNotificationService
  ) {}

  ngOnInit(): void {}


  deleteHorse(id: number) {
    this.service.deleteHorse(id).subscribe({
      next: () => {
        const horse = this.horses.find((h) => h.id === id);
        this.userNotificationService.addNotification({
          message: 'Horse "' + horse.name + '" successfully deleted',
          type: "success",
        });
        this.horses = this.horses.filter((item) => item.id !== id);
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
