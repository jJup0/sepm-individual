import { Component, OnInit } from "@angular/core";

import { Horse } from "../../dto/horse";
import { HorseService } from "src/app/service/horse.service";
import { HorseFormType } from "src/app/types/horseFormTypeEnum";
import { Subject } from "rxjs";
import { UserNotificationService } from "src/app/service/user-notification.service";
import { UserNotification } from "src/app/types/responseMessage";

@Component({
  selector: "app-add-horse",
  templateUrl: "./add-horse.component.html",
  styleUrls: ["./add-horse.component.scss"],
})
export class AddHorseComponent implements OnInit {
  FORMTYPE: HorseFormType = "add";
  horse?: Horse;

  messagesSubject = new Subject<UserNotification>();

  constructor(private service: HorseService, private userNotificationService: UserNotificationService) {}

  ngOnInit(): void {
    this.resetHorse();
  }

  emitEventToChild() {}

  resetHorse() {
    this.horse = {
      name: "",
      description: "",
      birthdate: new Date().toISOString().slice(0, 10),
      sex: null,
      owner: null,
      mother: null,
      father: null,
    };
  }

  submit(horse: Horse): void {
    this.horse = horse;
    this.postHorse();
  }

  postHorse(): void {
    this.service.addHorse(this.horse).subscribe({
      next: (horse) => {
        this.userNotificationService.addNotification({
          message: 'Horse "' + horse.name + '" successfully added',
          type: "success",
        });
        this.resetHorse();
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
