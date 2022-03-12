import { Component, OnInit } from "@angular/core";

import { Horse } from "../../dto/horse";
import { sexes } from "../../dto/horseSexEnum";
import { HorseService } from "src/app/service/horse.service";
import { HorseFormType } from "src/app/dto/horseFormTypeEnum";

@Component({
  selector: "app-add-horse",
  templateUrl: "./add-horse.component.html",
  styleUrls: ["./add-horse.component.scss"],
})
export class AddHorseComponent implements OnInit {
  FORMTYPE: HorseFormType = "add";
  submitted = false;
  SEXES = sexes;

  // Bug: birth input still buggy with display
  model: Horse = {
    name: "Horse",
    description: "",
    birthdate: new Date("01-01-2000"),
    sex: "female",
    owner: null,
    motherId: null,
    fatherId: null,
  };

  constructor(private service: HorseService) {}

  submit(): void {
    this.submitted = true;
    this.postHorse();
    this.model = {
      name: "Horse2",
      description: "",
      birthdate: new Date("1971-01-01"),
      sex: "female",
      owner: "",
    };
  }

  postHorse(): void {
    this.service.addHorse(this.model).subscribe({
      next: (data) => {
        console.log("added horse", data);
      },
      error: (error) => {
        console.error("Error adding horse", error.message);
        // TODO: show user
      },
    });
  }

  ngOnInit(): void {}
}
