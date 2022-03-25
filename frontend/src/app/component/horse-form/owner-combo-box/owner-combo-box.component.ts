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
import { Owner } from "src/app/dto/owner";
import { OwnerSearchDto } from "src/app/dto/ownerSearchDto";
import { OwnerService } from "src/app/service/owner.service";

@Component({
  selector: 'app-owner-combo-box',
  templateUrl: './owner-combo-box.component.html',
  styleUrls: ['./owner-combo-box.component.scss']
})
export class OwnerComboBoxComponent implements OnInit {
  @Input()
  ownedHorse: Horse;

  @Input()
  formType: HorseFormType;

  @Input()
  currentOwner: Horse;

  @Output()
  setOwnerEmitter = new EventEmitter<Owner>();

  searched = false;

  searchTerms = new Subject<OwnerSearchDto>();

  loadedOwners$!: Observable<Owner[]>;

  constructor(
    private ownerService: OwnerService
  ) {}

  ngOnInit(): void {


    this.loadedOwners$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: OwnerSearchDto) => this.ownerService.search(term))
    );
  }

  // Push a search term into the observable stream.
  search(searchString: string): void {
    this.searchTerms.next({
      name: searchString,
    });
    this.searched = true;
  }

  setOwner(parentStr: string) {
    const owner: Owner = JSON.parse(parentStr);
    this.setOwnerEmitter.emit(owner);
  }
}
