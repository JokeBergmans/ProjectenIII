// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  apiUrl: 'https://damiaanapp-api20201227140612.azurewebsites.net/api',
  //apiUrl: 'https://localhost:44349/api',
  staticFilesUrl: 'https://damiaanappapistoragefin.blob.core.windows.net/routes',
  locationIqUrl: 'https://eu1.locationiq.com/v1/reverse.php?key=pk.f5428eab2db1fb362f45a79991ac4a61'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
