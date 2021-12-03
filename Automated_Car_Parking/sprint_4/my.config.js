// @ts-check
/** @type {import('@playwright/test').PlaywrightTestConfig} */
const config = {  
	timeout: 120000,
	use: {       
		browserName: 'chromium',    
		headless: false,
	},
};
module.exports = config;
