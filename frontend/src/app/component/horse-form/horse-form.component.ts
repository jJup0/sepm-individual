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
  loadedParents$!: Observable<Horse[]>;
  searchTerm: HorseSearchDto = { searchTerm: "", sex: null };

  private searchTerms = new Subject<HorseSearchDto>();

  constructor(
    private route: ActivatedRoute,
    private horseService: HorseService
  ) {}

  // Push a search term into the observable stream.
  search(term: HorseSearchDto): void {
    this.searchTerms.next(term);
  }

  ngOnInit(): void {
    this.loadedParents$ = this.searchTerms.pipe(
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

  myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
  }
}
