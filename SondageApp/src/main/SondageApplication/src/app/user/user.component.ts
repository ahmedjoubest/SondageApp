import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import {FormArray, FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {SondageInfo} from '../services/sondage-info';
import {VoteInfo} from '../services/vote-info';
import {AuthService} from '../auth/auth.service';
import {TokenStorageService} from '../auth/token-storage.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  form: FormGroup;
  sondageInfo: SondageInfo;
  voteInfo: VoteInfo;
  board: string;
  errorMessage: string;
  isVote = false;
  isVoteFailed = false;
  votes;


  model_title: string;
  private formLieux: FormArray;
  private formDates: FormArray;
  selectedLieu: FormControl = new FormControl();
  selectedDate: FormControl = new FormControl();

  constructor(private userService: UserService,private fb: FormBuilder, private tokenStorage: TokenStorageService) { }

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

  onSubmit() {

    this.voteInfo = new VoteInfo(
      this.form.value.id,
      this.tokenStorage.getUsername(),
      this.selectedDate.value,
      this.selectedLieu.value);

    console.log(this.voteInfo);

      this.userService.createRessources("/api/sondage/vote",this.voteInfo)
        .subscribe( data => {
            console.log(data);
            this.isVote = true;
            this.isVoteFailed = false;
            this.getSondages();
          },
          error => {
            console.log(error);
            this.errorMessage = error.error.message;
            this.isVoteFailed = true;
          });

  }

  voteSondage(sondage: SondageInfo) {
    this.model_title = "Voter sur le sondage " + sondage.titre;

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

  private getSondages() {
    this.userService.getRessources("/api/vote/find/" + this.tokenStorage.getUsername())
      .subscribe(data=>{
        this.votes = data;
        console.log(this.votes);
      },err=>{
        console.log(err);
      })
  }

  nouveauSondage() {
    this.model_title= "Ajouter nouveau vote";

    this.form = this.fb.group({
      titre: [],
      description: [],
      lieux: this.fb.array([this.fb.group({lieu:''})]),
      dates: this.fb.array([this.fb.group({date:''})])
    })
  }

}
