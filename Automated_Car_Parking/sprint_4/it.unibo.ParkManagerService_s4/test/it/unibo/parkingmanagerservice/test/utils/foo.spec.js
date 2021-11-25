/*
const { test, expect } = require('@playwright/test');
test('basic test', async ({ page }) => {  
await page.goto('https://playwright.dev/');  
const title = page.locator('.navbar__inner .navbar__title');  
await expect(title).toHaveText('Playwright');
page.click('text=GET STARTED');
await page.waitForTimeout(5000)
//NB to use Xpath it' necessary that Xpath begin with // or ..'
const title2 = page.locator('//html/body/div/div[2]/div/main/div/div/div[1]/div/article/div/header/h1');  
await expect(title2).toHaveText('Getting started');
});
*/

const { test, expect } = require('@playwright/test');
test('basic test', async ({ page }) => { 
  await page.goto('http://localhost:8081/');
  await page.waitForTimeout(3000)
  await page.click('button:has-text("Enter Request")');
  //await expect(page.url()).toHaveText('http://localhost:8081/reqenter');
  await page.waitForTimeout(3000)
  await page.click('button:has-text("CarEnter Request")'); //carenter
  //await expect(page.url(), 'http://localhost:8081/carenter');
  await page.waitForTimeout(8000)
  /*
  await page.click('//html/body/div[2]/div[2]/span/pre');
  await page.dblclick('//html/body/div[2]/div[2]/span/pre');
  await page.waitForTimeout(1000)
  await page.keyboard.press('Control+c');
  await page.waitForTimeout(1000)
  await page.dblclick('//*[@id="tokenid"]')
  await page.keyboard.press('Control+v');
  */
  //alternative
	//const token = await page.$eval("#email30", (el) => el.value);
	const value = await page.evaluate(() => document.getElementById("infoDisplay").textContent) 
	token = value.slice(7)
	//console.log(token)
	await page.fill('//*[@id="tokenid"]', token);
  //----------------------------------------------------------
  await page.waitForTimeout(3000)
  await page.click('button:has-text("Submit your TOKENID")');
  //await expect(page.url(), 'http://localhost:8081/pickup');
  await page.waitForTimeout(20000)

});
