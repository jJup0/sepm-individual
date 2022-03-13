import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import { Horse } from "src/app/dto/horse";
import { sexes } from "../../dto/horseSexEnum";
import { HorseFormType } from "src/app/dto/horseFormTypeEnum";

@Component({
  selector: "app-horse-form",
  templateUrl: "./horse-form.component.html",
  styleUrls: ["./horse-form.component.scss"],
})
export class HorseFormComponent implements OnInit {
  @Input()
  formType: HorseFormType;

  @Input()
  horse: Horse;

  @Output()
  submitPressed = new EventEmitter<Horse>();

  SEXES = sexes;
  SUBMIT_BUTTON_TEXT: string;
  submitted = false;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {}

  submit() {
    this.submitPressed.emit(this.horse);
    this.submitted = true;
  }
}
