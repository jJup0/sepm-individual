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

  motherSeachTerm: string;

  SEXES = sexes;
  SUBMIT_BUTTON_TEXT: string;
  submitted = false;
  loadedMothers$!: Observable<Horse[]>;
  loadedFathers$!: Observable<Horse[]>;

  searchTerm: HorseSearchDto = {
    searchTerm: "",
    sex: null,
    bornBefore: new Date(),
  };

  private motherSearchTerms = new Subject<HorseSearchDto>();
  private fatherSearchTerms = new Subject<HorseSearchDto>();

  constructor(
    private route: ActivatedRoute,
    private horseService: HorseService
  ) {}

  // Push a search term into the observable stream.
  search(term: HorseSearchDto, parentType: ParentType): void {
    if (parentType === "mother") {
      this.motherSearchTerms.next(term);
    } else {
      this.fatherSearchTerms.next(term);
    }
  }

  ngOnInit(): void {
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

  returnFathers() {
    return this.loadedFathers$;
  }

  setParent(parentStr: string, parentType: ParentType) {
    const parent = JSON.parse(parentStr);
    console.log("setting parents: " + parent + "has type:" + typeof parent);
    if (parentType === "mother") {
      this.horse.mother = parent;
    } else if (parentType === "father") {
      this.horse.father = parent;
    }
  }

  horseBirthdayISO() {
    return this.horse.birthdate.toISOString().slice(0, 10);
  }

  todaysDateISO() {
    return new Date().toISOString().slice(0, 10);
  }

  setBirthdate(ddmmyyyy: string) {
    this.horse.birthdate = new Date(ddmmyyyy);
  }
}
