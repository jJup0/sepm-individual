import { HorseSex } from "./horseSexEnum";

export interface HorseSearchDto {
  searchTerm?: string;
  sex?: HorseSex;
  bornBefore?: Date;
}
