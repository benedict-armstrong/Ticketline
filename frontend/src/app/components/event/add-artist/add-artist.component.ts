import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Artist} from '../../../dtos/artist';
import {ArtistService} from '../../../services/artist.service';

@Component({
  selector: 'app-add-artist',
  templateUrl: './add-artist.component.html',
  styleUrls: ['./add-artist.component.scss']
})
export class AddArtistComponent implements OnInit {
  @Output() artistChanged = new EventEmitter<Artist>();
  addArtistForm: FormGroup;
  selectArtistForm: FormGroup;
  addNewArtist = false;
  existingArtists: Artist[];
  filteredArtists: Artist[];
  submitted = false;

  constructor(private formBuilder: FormBuilder, private artistService: ArtistService) { }

  ngOnInit(): void {
    this.addArtistForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: [''],
      selectExistingArtist: ['']
    });

    this.selectArtistForm = this.formBuilder.group({
      filterFirstName: [''],
      filterLastName: [''],
      selectedArtist: ['']
    });

    this.artistService.getArtists(0, 1000).subscribe((response) => {
      this.existingArtists = response;
      this.filteredArtists = this.existingArtists;
    });
  }

  addArtist(): void {
    this.submitted = true;

    if (this.addArtistForm.valid) {
      const addedArtist = new Artist(null,
        this.addArtistForm.value.firstName,
        this.addArtistForm.value.lastName,
        null
      );

      this.artistService.addArtist(addedArtist).subscribe((response) => {
        this.existingArtists.push(response);
        this.addNewArtist = false;
        this.selectArtistForm.value.selectedArtist = response;
        this.artistSelected();
      });
    }
  }

  /**
   * filter the array of artists by lastname and firstname
   */
  filterArtists(): void {
    const firstNameFilter = this.selectArtistForm.value.filterFirstName;
    const lastNameFilter = this.selectArtistForm.value.filterLastName;
    this.filteredArtists = this.existingArtists.filter(
      (value) => {
        if (firstNameFilter !== '') {
          if (!value.firstName.includes(firstNameFilter)) {
            return false;
          }
        }

        if (lastNameFilter !== '') {
          if (!value.lastName.includes(lastNameFilter)) {
            return false;
          }
        }

        return true;
    });
  }

  artistSelected(): void {
    this.artistChanged.emit(this.selectArtistForm.value.selectedArtist);
  }
}
