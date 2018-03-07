import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'ebx-app-url-mapper',
  templateUrl: './app-url-mapper.component.html',
  styleUrls: ['./app-url-mapper.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AppUrlMapperComponent implements OnInit {

  constructor(private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    console.log("Url fragment: " + this.route.snapshot.fragment);
    console.log("Url params: " + this.route.snapshot.queryParams);
    let urlFragment = this.route.snapshot.fragment;
    this.router.navigateByUrl(urlFragment ? urlFragment : '');
  }

}
