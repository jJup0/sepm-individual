import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import {
  debounceTime,
  distinctUntilChanged,
  Observable,
  Subject,
  switchMap,
} from "rxjs";
import { Horse } from "src/app/dto/horse";
import { HorseFormType } from "src/app/dto/horseFormTypeEnum";
import { HorseSearchDto } from "src/app/dto/horseSearchDto";
import { HorseSex } from "src/app/dto/horseSexEnum";
import { HorseService } from "src/app/service/horse.service";

@Component({
  selector: "app-parent-combo-box",
  templateUrl: "./parent-combo-box.component.html",
  styleUrls: ["./parent-combo-box.component.scss"],
})
export class ParentComboBoxComponent implements OnInit {
  @Input()
  parentSex: HorseSex;

  @Input()
  childHorse: Horse;

  @Input()
  formType: HorseFormType;

  @Input()
  parentObject: Horse;

  @Output()
  setParentEmitter = new EventEmitter<Horse>();

  parentName: string;
  searched = false;

  searchTerms = new Subject<HorseSearchDto>();

  loadedParents$!: Observable<Horse[]>;

  constructor(
    private horseService: HorseService
  ) {}

  ngOnInit(): void {
    if (this.parentSex === "female") {
      this.parentName = "mother";
    } else {
      this.parentName = "father";
    }

    this.loadedParents$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: HorseSearchDto) => this.horseService.search(term))
    );
  }

  // Push a search term into the observable stream.
  search(searchString: string): void {
    this.searchTerms.next({
      name: searchString,
      sex: this.parentSex,
      bornBefore: this.childHorse.birthdate,
      limit: 5,
    });
    this.searched = true;
  }

  setParent(parentStr: string) {
    const parent: Horse = JSON.parse(parentStr);
    console.log(JSON.stringify(parent));
    this.setParentEmitter.emit(parent);
  }
}
