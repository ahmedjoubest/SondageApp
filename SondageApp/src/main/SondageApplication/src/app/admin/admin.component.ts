import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import {SondageInfo} from '../services/sondage-info';
import { FormBuilder, FormGroup, FormArray, FormControl } from '@angular/forms';
import {el} from '@angular/platform-browser/testing/src/browser_util';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  form: FormGroup;
  sondageInfo: SondageInfo;
  board: string;
  errorMessage: string;
  isSondage = false;
  isSondageFailed = false;
  sondages;
  private lieuxNoms: string[] = [];
  private datesNoms: string[] = [];

  public popoverTitle: string = 'Supprimer sondage';
  public popoverMessage: string = 'êtes-vous sûr de vouloir supprimer ce sondage';
  model_title: string;
  edit : boolean = false;
  private formLieux: FormArray;
  private formDates: FormArray;

  constructor(private userService: UserService,private fb: FormBuilder) { }

  ngOnInit() {
    /* Initiate the form structure */
    this.nouveauSondage();

    this.getSondages();
  }

  get lieux() {
    return this.form.get('lieux') as FormArray;
  }

  get dates() {
    return this.form.get('dates') as FormArray;
  }

  addLieu(l = '') {
    this.lieux.push(this.fb.group({lieu:l}));
  }

  addDate() {
    this.dates.push(this.fb.group({date:''}));
  }

  deleteLieu(index) {
    this.lieux.removeAt(index);
  }

  deleteDate(index) {
    this.dates.removeAt(index);
  }

  onSubmit() {
    console.log(this.form);

    this.lieuxNoms = [];
    this.form.value.lieux.forEach(l=>{
      this.lieuxNoms.push(l.lieu);
    })

    this.datesNoms = [];
    this.form.value.dates.forEach(l=>{
      this.datesNoms.push(l.date);
    })

    this.sondageInfo = new SondageInfo(
      this.form.value.id,
      this.form.value.titre,
      this.form.value.description,
      this.lieuxNoms,
      this.datesNoms);

    if(!this.edit){
      this.userService.createRessources("/api/sondage/add",this.sondageInfo).subscribe(
        data => {
          console.log(data);
          this.isSondage = true;
          this.isSondageFailed = false;
          this.getSondages();
        },
        error => {
          console.log(error);
          this.errorMessage = error.error.message;
          this.isSondageFailed = true;
        }
      );
    }
    else {
      this.userService.updateRessources("/api/sondage/update/"+this.sondageInfo.id,this.sondageInfo)
        .subscribe( data => {
            console.log(data);
            this.isSondage = true;
            this.isSondageFailed = false;
            this.getSondages();
          },
          error => {
            console.log(error);
            this.errorMessage = error.error.message;
            this.isSondageFailed = true;
          });
    }

  }

  editSondage(sondage: SondageInfo) {
    this.model_title = "Modifier le sondage " + sondage.id;
    this.edit = true;

    this.formLieux = this.fb.array([]);
    sondage.lieux.forEach(l=>{
      this.formLieux.push(this.fb.group({lieu:l}));
    })

    this.formDates = this.fb.array([]);
    sondage.dates.forEach(l=>{
      this.formDates.push(this.fb.group({date:l}));
    })

    this.form = this.fb.group({
      id : [sondage.id],
      titre: [sondage.titre],
      description: [sondage.description],
      lieux: this.formLieux,
      dates: this.formDates
    })

  }

  deleteSondage(sondage: SondageInfo) {
    this.userService.deleteRessources("/api/sondage/delete/"+ sondage.id)
      .subscribe( data => {
        this.sondages = this.sondages.filter(u => u !== sondage);
      });
  }

  private getSondages() {
    this.userService.getRessources("/api/sondage/find")
      .subscribe(data=>{
        this.sondages = data;
      },err=>{
        console.log(err);
      })
  }

  nouveauSondage() {
    this.model_title= "Ajouter nouveau sondage";
    this.edit = false;

    this.form = this.fb.group({
      titre: [],
      description: [],
      lieux: this.fb.array([this.fb.group({lieu:''})]),
      dates: this.fb.array([this.fb.group({date:''})])
    })
  }
}
