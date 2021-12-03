const { test, expect } = require('@playwright/test');
test('ManagerGUI_test', async ({ page }) => { 
  await page.goto('http://localhost:8083');
  
  await page.waitForTimeout(1000)
  
  const stopbtn 		= await page.locator('button:has-text("STOP Trolley")');  
  const resumebtn 		= await page.locator('button:has-text("RESUME Trolley")');  
  const trolleystatus 	= await page.locator('//*[@id="statusDisplay"]') //trolleyStatus
  const temp 			= await page.locator('//*[@id="tempDisplay"]')   //temperature
  
  await page.waitForFunction(() => {
	return document.querySelector('#stopbtn').hasAttribute('disabled')==false
	});
  await page.waitForTimeout(500)
  stopbtn.click()
  await page.waitForTimeout(1000)
  await expect(trolleystatus).toContainText('STOPPED')
  
  await page.waitForFunction(() => {
	return document.querySelector('#resumebtn').hasAttribute('disabled')==false
	});
  await page.waitForTimeout(500)
  await resumebtn.click()
  await page.waitForTimeout(1000)
  await expect(trolleystatus).not.toContainText('STOPPED')
  

});
