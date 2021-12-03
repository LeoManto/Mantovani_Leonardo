const { test, expect } = require('@playwright/test');
test('ClientGUI_test', async ({ page }) => { 
  await page.goto('http://localhost:8081/');
  await page.waitForTimeout(3000)
  await page.click('button:has-text("Enter Request")');

  const slot = await page.locator('//*[@id="infoDisplay"]');
await expect(slot).toContainText('SLOTNUM:');
  
  await page.waitForTimeout(4000)
  await page.click('button:has-text("CarEnter Request")'); //carenter
  await page.waitForTimeout(8000)
  const value = await page.evaluate(() => document.getElementById("infoDisplay").textContent) 
	
	token = value.slice(7)
	const tok = await page.locator('//*[@id="infoDisplay"]');
await expect(tok).toContainText('TOKEN:');
  
	await page.fill('//*[@id="tokenid"]', token);

  await page.waitForTimeout(3000)
  await page.click('button:has-text("Submit your TOKENID")');
  
	const after = await page.locator('//*[@id="adv"]');
await expect(after).toContainText('Token sended');
  

});

