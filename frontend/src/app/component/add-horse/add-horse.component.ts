import { Component, OnInit } from "@angular/core";

import { Horse } from "../../dto/horse";
import { HorseSex } from "../../dto/horseSex";
import { HorseService } from "src/app/service/horse.service";

@Component({
  selector: "app-add-horse",
  templateUrl: "./add-horse.component.html",
  styleUrls: ["./add-horse.component.scss"],
})
export class AddHorseComponent implements OnInit {
  sexes: HorseSex[] = ["male", "female"];

  // Bug: birth input still buggy with display
  model: Horse = {
    name: "",
    description: "",
    birthdate: new Date("01-01-2000"),
    sex: "male",
    owner: "",
  };

  submitted = false;

  constructor(private service: HorseService) {}

  onSubmit() {
    this.submitted = true;

    this.model = {
      name: "",
      description: "",
      birthdate: new Date("1970-01-01"),
      sex: "female",
      owner: "",
    };
  }

  postHorse() {
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
