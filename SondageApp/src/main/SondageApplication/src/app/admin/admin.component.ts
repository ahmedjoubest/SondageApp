import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import {SignUpInfo} from '../auth/signup-info';
import { FormBuilder, FormGroup, FormArray, FormControl } from '@angular/forms';
import {el} from '@angular/platform-browser/testing/src/browser_util';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  mode = "-1";
  dropdownList = [];
  selectedItems = [];
  dropdownSettings = {};

  onItemSelect(item: any) {
    console.log(item);
  }
  onSelectAll(items: any) {
    console.log(items);
  }

  form: FormGroup;
  user: SignUpInfo;
  board: string;
  errorMessage: string;
  isUser = false;
  isUserFailed = false;
  users;

  public popoverTitle: string = 'Supprimer utilisateur';
  public popoverMessage: string = 'êtes-vous sûr de vouloir supprimer cet utilisateur';
  model_title: string;
  edit : boolean = false;

  constructor(private userService: UserService,private fb: FormBuilder) { }

  ngOnInit() {
    this.dropdownList = ["admin","user"];
    this.dropdownSettings = {
      singleSelection: false,
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 2,
      allowSearchFilter: true
    };
    this.getUsers();
  }

  onSubmit() {
    console.log(this.form);


  }

  deleteUser(user: SignUpInfo) {
    this.userService.deleteRessources("/api/auth/delete/"+ user.id)
      .subscribe( data => {
        this.users = this.users.filter(u => u !== user);
      });
  }

  private getUsers() {
    this.userService.getRessources("/api/auth/find")
      .subscribe(data=>{
        this.users = data;
      },err=>{
        console.log(err);
      })
  }

  editUser(user:SignUpInfo,roles): void {
    this.mode = user.id.toString();

    this.dropdownList = ["admin","user"];

    this.dropdownSettings = {
      singleSelection: false,
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: true
    };

    this.selectedItems = [];
    for (let element of roles) {
      if(element.name == "ROLE_ADMIN")
        this.selectedItems.push("admin");
      else if(element.name == "ROLE_USER")
        this.selectedItems.push("user");
    }
  }

  updateUser(user: any, roles: any[]) {
    this.mode = "-1";
    console.log(user);
    console.log(roles);
    this.user = new SignUpInfo(
      user.name,
      user.username,
      user.email,
      user.password,
      );
    this.user.role = roles;

    this.userService.updateRessources("/api/auth/update/"+user.id,this.user)
      .subscribe( data => {
          console.log(data);
          this.getUsers();
        },
        error => {
          console.log(error);
        });
  }
}
