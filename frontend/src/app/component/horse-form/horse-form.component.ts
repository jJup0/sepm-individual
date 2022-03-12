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
  submitPressed = new EventEmitter<boolean>();

  SEXES = sexes;
  SUBMIT_BUTTON_TEXT: string;
  saved = false;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    const addFormType: HorseFormType = "add";
    this.SUBMIT_BUTTON_TEXT =
      this.formType === addFormType ? "Add Horse" : "Update Horse";
    console.log("submit button text: " + this.SUBMIT_BUTTON_TEXT);
    // console.log("form type: " + this.formType);
    // console.log(
    //   `horse form is at id: ${JSON.stringify(this.route.snapshot.toString())}`
    // );
  }

  submit(valid: boolean) {
    this.submitPressed.emit(valid);
    this.saved = true;
  }
}
