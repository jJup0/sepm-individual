import { Component, Input, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Horse } from "src/app/dto/horse";
import { HorseFormType } from "src/app/types/horseFormTypeEnum";
import { HorseService } from "src/app/service/horse.service";
import { UserNotificationService } from "src/app/service/user-notification.service";

@Component({
  selector: "app-detail-horse",
  templateUrl: "./detail-horse.component.html",
  styleUrls: ["./detail-horse.component.scss"],
})
export class DetailHorseComponent implements OnInit {
  FORMTYPE: HorseFormType = "detail";
  horse?: Horse;
  deleted = false;

  constructor(
    private service: HorseService,
    private route: ActivatedRoute,
    private horseService: HorseService,
    private userNotificationService: UserNotificationService
  ) {}

  ngOnInit(): void {
    this.getHorse();
  }

  getHorse(): void {
    this.route.params.subscribe({
      next: (params) => {
        this.horseService.getHorse(params.id).subscribe({
          next: (horse) => {
            this.horse = horse;
          },
        });
      },
      error: (error) => {
        this.userNotificationService.addNotification({
          message: error.error.message,
          type: "error",
        });
      },
    });
  }

  deleteHorse(id: number) {
    this.service.deleteHorse(id).subscribe();
    this.deleted = true;
  }
}
