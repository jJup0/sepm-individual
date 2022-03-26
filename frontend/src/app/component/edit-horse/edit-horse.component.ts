import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import { Horse } from "src/app/dto/horse";
import { dtoToIdReferences } from "src/app/dto/horseDTOmapper";
import { HorseService } from "src/app/service/horse.service";
import { UserNotificationService } from "src/app/service/user-notification.service";
import { HorseFormType } from "src/app/types/horseFormTypeEnum";

@Component({
  selector: "app-edit-horse",
  templateUrl: "./edit-horse.component.html",
  styleUrls: ["./edit-horse.component.scss"],
})
export class EditHorseComponent implements OnInit {
  horse?: Horse;
  FORMTYPE: HorseFormType = "edit";

  constructor(
    private route: ActivatedRoute,
    private horseService: HorseService,
    private userNotificationService: UserNotificationService
  ) {}

  ngOnInit(): void {
    this.getHorse();
  }
  getHorse(): void {
    const id = Number(this.route.snapshot.paramMap.get("id"));
    this.horseService.getHorse(id).subscribe({
      next: (horse) => {
        this.userNotificationService.addNotification({
          message: "Succesfully updated horse!",
          type: "success",
        });
        this.horse = horse;
      },
      error: (error) => {
        this.userNotificationService.addNotification({
          message: error.error.message,
          type: "error",
        });
      },
    });
  }

  // TODO add back button

  save(horse: Horse): void {
    this.horse = horse;
    this.horseService.updateHorse(dtoToIdReferences(this.horse)).subscribe();
  }
}
