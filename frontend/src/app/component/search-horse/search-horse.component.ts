import { Component, OnInit } from "@angular/core";
import { Horse } from "src/app/dto/horse";
import { HorseFormType } from "src/app/types/horseFormTypeEnum";
import { HorseSearchDto } from "src/app/dto/horseSearchDto";
import { HorseService } from "src/app/service/horse.service";
import { UserNotificationService } from "src/app/service/user-notification.service";

@Component({
  selector: "app-search-horse",
  templateUrl: "./search-horse.component.html",
  styleUrls: ["./search-horse.component.scss"],
})
export class SearchHorseComponent implements OnInit {
  FORMTYPE: HorseFormType = "search";
  searchFields: Horse = {
    name: null,
    description: null,
    birthdate: null,
    sex: null,
    owner: null,
  };
  matchedHorses: Horse[] = [];

  constructor(
    private service: HorseService,
    private userNotificationService: UserNotificationService
  ) {}

  ngOnInit(): void {}

  search(horse: Horse): void {
    const horseSearchDto: HorseSearchDto = {
      name: horse.name,
      description: horse.description,
      bornAfter: horse.birthdate,
      sex: horse.sex,
      bornBefore: null,
      owner: horse.owner,
    };

    this.service.search(horseSearchDto).subscribe({
      next: (horses) => {
        if (horses.length === 0) {
          this.userNotificationService.addNotification({
            message: "Could not find any horses",
            type: "warning",
          });
        }
        this.matchedHorses = horses;
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
