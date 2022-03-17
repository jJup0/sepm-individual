import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { Horse } from "src/app/dto/horse";
import { HorseService } from "src/app/service/horse.service";

type ParentType = "mother" | "father";

@Component({
  selector: "app-horse-familytree-row",
  templateUrl: "./horse-familytree-row.component.html",
  styleUrls: ["./horse-familytree-row.component.scss"],
})
export class HorseFamilytreeRowComponent implements OnInit {
  @Input()
  horse: Horse;

  @Output()
  deletedParent = new EventEmitter<Horse>();

  opened = false;

  constructor(private service: HorseService) {}

  ngOnInit(): void {}

  toggleCollapse() {
    this.opened = !this.opened;
  }
  deleteHorse() {
    if (this.horse) {
      this.service.deleteHorse(this.horse.id).subscribe();
    }
    this.deletedParent.emit(this.horse);
    console.log("deleted " + this.horse.name);
    this.horse = null;
  }

  removeParent(parentType: ParentType) {
    if (this.horse) {
      if (parentType === "mother") {
        this.horse.mother = null;
      } else if (parentType === "father") {
        this.horse.father = null;
      } else {
        // TODO gone completely wrong
      }
    }
  }
}
