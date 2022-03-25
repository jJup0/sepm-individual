import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import { Horse } from "src/app/dto/horse";
import { sexes } from "../../dto/horseSexEnum";
import { HorseFormType } from "src/app/dto/horseFormTypeEnum";
import {
  debounceTime,
  distinctUntilChanged,
  Observable,
  Subject,
  switchMap,
} from "rxjs";

import { HorseService } from "src/app/service/horse.service";
import { HorseSearchDto } from "src/app/dto/horseSearchDto";

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
  loadedMothers$!: Observable<Horse[]>;
  loadedFathers$!: Observable<Horse[]>;

  searchTerm: HorseSearchDto = {
    name: null,
    sex: null,
    bornBefore: null,
  };

  private motherSearchTerms = new Subject<HorseSearchDto>();
  private fatherSearchTerms = new Subject<HorseSearchDto>();

  constructor(
    private route: ActivatedRoute,
    private horseService: HorseService
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

    this.loadedMothers$ = this.motherSearchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: HorseSearchDto) => this.horseService.search(term))
    );
    this.loadedFathers$ = this.fatherSearchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: HorseSearchDto) => this.horseService.search(term))
    );
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

  todaysDateISO() {
    return new Date().toISOString().slice(0, 10);
  }
}
