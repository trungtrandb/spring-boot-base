import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NbTokenService } from '@nebular/auth';
import { NbMenuService } from '@nebular/theme';
import { AnalyticsService } from './@core/utils/analytics.service';
import { SeoService } from './@core/utils/seo.service';

@Component({
  selector: 'ngx-app',
  template: '<router-outlet></router-outlet>',
})
export class AppComponent implements OnInit {

  constructor(private analytics: AnalyticsService,
    private seoService: SeoService,
    private menuService: NbMenuService,
    private nbTokenService: NbTokenService,
    private router: Router) {
    this.menuService.onItemClick()
      .subscribe((event) => {
        this.onContextItemSelection(event.item.title);
      });
  }

  ngOnInit(): void {
    this.analytics.trackPageViews();
    this.seoService.trackCanonicalChanges();
  }

  onContextItemSelection(title) {
    if (title === 'Logout') {
      this.nbTokenService.clear();
      this.router.navigate(['/auth/logout']);
    }
  }
}
