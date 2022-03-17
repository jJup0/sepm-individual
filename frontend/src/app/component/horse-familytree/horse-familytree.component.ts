import { Component, Input, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Horse } from "src/app/dto/horse";
import { HorseService } from "src/app/service/horse.service";

@Component({
  selector: "app-horse-familytree",
  templateUrl: "./horse-familytree.component.html",
  styleUrls: ["./horse-familytree.component.scss"],
})
export class HorseFamilytreeComponent implements OnInit {
  horse: Horse;
  generationsToGet = 3;
  constructor(
    private route: ActivatedRoute,
    private horseService: HorseService
  ) {}

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.route.params.subscribe({
      next: (params) => {
        this.horseService
          .getHorseFamilytree(params.id, this.generationsToGet)
          .subscribe({
            next: (horse) => {
              this.horse = horse;
            },
          });
      },
    });
  }
}
