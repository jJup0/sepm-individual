import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import { Horse } from "src/app/dto/horse";
import { sexes } from "../../types/horseSexEnum";
import { HorseFormType } from "src/app/types/horseFormTypeEnum";
import {
  debounceTime,
  distinctUntilChanged,
  Observable,
  Subject,
  switchMap,
} from "rxjs";

import { HorseService } from "src/app/service/horse.service";
import { HorseSearchDto } from "src/app/dto/horseSearchDto";
import { Owner } from "src/app/dto/owner";

type ParentType = "mother" | "father";

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
  SUBMIT_SUCCESS_TEXT: string;
  submitted = false;

  parentSearchParameters: HorseSearchDto = {
    name: null,
    sex: null,
    bornBefore: null,
  };


  constructor(
  ) {}

  ngOnInit(): void {
    switch (this.formType) {
      case "add":
        this.SUBMIT_BUTTON_TEXT = "Add horse";
        this.SUBMIT_SUCCESS_TEXT = "Horse added!";
        break;
      case "edit":
        this.SUBMIT_BUTTON_TEXT = "Update horse";
        this.SUBMIT_SUCCESS_TEXT = "Horse updated";
        break;
      case "search":
        this.SUBMIT_BUTTON_TEXT = "Search";
        this.SUBMIT_SUCCESS_TEXT = null;
        break;
      case "detail":
        this.SUBMIT_BUTTON_TEXT = null;
        this.SUBMIT_SUCCESS_TEXT = null;
        break;
    }

  }

  submit() {
    this.submitPressed.emit(this.horse);
    this.submitted = true;
  }

  setParent(parent: Horse, parentType: ParentType) {
    if (parentType === "mother") {
      this.horse.mother = parent;
    } else if (parentType === "father") {
      this.horse.father = parent;
    }
  }

  setOwner(owner: Owner) {
    this.horse.owner = owner;
  }

  todaysDateISO() {
    return new Date().toISOString().slice(0, 10);
  }
}
