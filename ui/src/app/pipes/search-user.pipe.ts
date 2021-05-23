import { Pipe, PipeTransform } from '@angular/core';
import {User} from "../models/user.model";
import {Observable} from "rxjs";
import {filter, map} from "rxjs/operators";

@Pipe({
  name: 'searchUser'
})
export class SearchUserPipe implements PipeTransform {

  transform(value: Observable<User[]>, name: string): Observable<User[]> {
    let result: Observable<User[]> = value.pipe(map((users: User[]) => {
      return users.filter((user: User) => {
        return user.name.toLowerCase().includes(name.toLowerCase());
      })
    }));

    return result;


  }

}
