# Testing GPA Calculator on Another Device

## Pre-Test Checklist

### Files Ready for Distribution
✅ **Portable Package:** `dist/` folder (created successfully)
✅ **Contains:**
   - GPA-Calculator.bat (launcher)
   - gpa-calculator.jar (application with bug fix)
   - lib/ (8 JavaFX dependency files)
   - README.txt (user instructions)

---

## How to Test on Another Device

### Step 1: Prepare Distribution Package
On your current device (this PC):

```powershell
# Navigate to project folder
cd C:\Users\User\IdeaProjects\Kaif_2207025_Gpa_Calculator_project

# Zip the dist folder
Compress-Archive -Path dist\* -DestinationPath GPA-Calculator-Portable.zip -Force
```

### Step 2: Transfer to Test Device
- Copy `GPA-Calculator-Portable.zip` to USB drive, cloud storage, or network share
- Transfer to the other device

### Step 3: Test on Target Device

#### On the Test Device:
1. **Extract the zip file** to any location (e.g., Desktop, Documents)
2. **Open the extracted folder**
3. **Double-click `GPA-Calculator.bat`**

---

## Expected Test Scenarios

### Scenario A: Device HAS Java 21
**Expected Result:**
- ✅ Application launches immediately
- ✅ Home screen appears with modern gradient design
- ✅ "Start Calculating →" button is visible

**Test the Bug Fix:**
1. Click "Start Calculating →"
2. Try clicking "Add Course" button WITHOUT setting target credits
3. ✅ **Expected:** Button should be disabled (grayed out)
4. Set target credits (e.g., 12)
5. Click "Set Target" button
6. ✅ **Expected:** Add Course button becomes enabled
7. ✅ **Expected:** Alert says "Target credits set to 12.0. You can now add courses."
8. Add courses and verify they don't exceed target

### Scenario B: Device DOES NOT have Java 21
**Expected Result:**
- ❌ Error message appears:
  ```
  ERROR: Java is not installed or not in PATH.
  
  Please install Java 21 or higher from:
  https://adoptium.net/
  ```

**Fix:** Install Java 21 from https://adoptium.net/ and try again

---

## Quick Test Commands (For Testing on Different Windows Devices)

### Test 1: Check if Java is installed
```cmd
java -version
```
**Expected:** Should show "openjdk version 21" or similar

### Test 2: Check Java PATH
```cmd
where java
```
**Expected:** Should show path to java.exe

### Test 3: Manual launch (if bat file doesn't work)
```cmd
cd [extracted-folder-path]
java --module-path lib --add-modules javafx.controls,javafx.fxml -cp gpa-calculator.jar com.kaif.gpacalculator.GpaCalculatorApp
```

---

## Testing Different Device Types

### Test Device Types:
1. ✅ **Same network PC** - Easiest to test via network share
2. ✅ **Laptop/Desktop with USB transfer**
3. ✅ **Virtual Machine** - Test clean Windows install
4. ✅ **Friend's/Classmate's PC** - Real-world test

### Recommended Test Sequence:
1. Device with Java 21 → Should work perfectly
2. Device without Java → Should show error (expected)
3. Device with older Java (e.g., Java 8, 11) → Should show error or fail
4. Fresh Windows 10/11 PC → Install Java 21, then test

---

## Verification Checklist

After launching on test device, verify:

- [ ] Application window opens (800x600 or current size)
- [ ] Home screen displays with gradient background
- [ ] Feature cards show icons (📊 🎯 ✨)
- [ ] "Start Calculating →" button works
- [ ] Course Entry screen loads
- [ ] Target credits input is enabled
- [ ] Course entry fields are DISABLED initially (bug fix)
- [ ] Setting target credits ENABLES course entry fields (bug fix)
- [ ] Can add courses after setting target
- [ ] Cannot exceed target credits
- [ ] Calculate button disabled until target met
- [ ] Results screen shows GPA correctly
- [ ] All navigation buttons work
- [ ] Window can be resized/maximized
- [ ] Window size persists across screens

---

## Troubleshooting on Test Device

### Problem: "Java is not installed"
**Solution:** 
```
1. Download Java 21 from: https://adoptium.net/
2. Install (next, next, finish)
3. Restart terminal/command prompt
4. Try GPA-Calculator.bat again
```

### Problem: "JavaFX runtime components are missing"
**Solution:** This shouldn't happen with the dist package. If it does:
- Verify all 8 JAR files exist in lib/ folder
- Re-extract the zip file
- Try manual launch command (see Test 3 above)

### Problem: Application launches but looks broken
**Possible causes:**
- Display scaling issues
- Missing fonts
- Graphics driver issues
**Try:** Resize window, check if modern UI appears

### Problem: Bat file does nothing
**Solution:**
- Right-click bat file → Edit
- Check if paths are correct
- Run from command prompt to see errors:
  ```cmd
  cd [folder-path]
  GPA-Calculator.bat
  ```

---

## Alternative: Create Installer for True Portability

If you want to test on devices **without Java**:

### Create Self-Contained Installer:
```batch
.\build-installer.bat
```

This creates: `release\GPA Calculator-1.0.exe`

**Installer includes Java runtime** → Works on ANY Windows device without Java!

**File size:** ~200 MB (includes Java 21 runtime)
**User experience:** Install once, run anywhere

---

## Test Report Template

Copy this and fill out after testing:

```
=== GPA CALCULATOR TEST REPORT ===

Test Date: _______________
Tester Name: _______________

Test Device Info:
- OS: Windows ___ (10/11)
- Java Version: ___ (java -version output)
- RAM: ___ GB
- Display Resolution: ___________

Test Results:
[ ] Application launched successfully
[ ] Home screen displayed correctly
[ ] Bug fix verified: Course entry disabled without target
[ ] Bug fix verified: Course entry enabled after target set
[ ] Cannot add courses exceeding target
[ ] GPA calculation works correctly
[ ] All screens navigate properly
[ ] Window resizing works
[ ] Application closes cleanly

Issues Found:
1. ______________________________
2. ______________________________

Overall Status: [ ] PASS  [ ] FAIL

Notes:
_________________________________
_________________________________
```

---

## Quick Copy-Paste for Testing

Share this with testers:

```
GPA Calculator - Quick Test Instructions

1. Extract GPA-Calculator-Portable.zip
2. Open extracted folder
3. Double-click GPA-Calculator.bat
4. If error about Java: Install from https://adoptium.net/
5. Test: Set target credits FIRST, then add courses
6. Verify you cannot exceed target credits
7. Calculate GPA and check results

Report any issues!
```

---

## Success Criteria

✅ **Test PASSES if:**
- Application launches on device with Java 21
- Course entry is initially disabled
- Course entry enables after setting target
- Cannot exceed target credits
- GPA calculates correctly
- All features work as designed

❌ **Test FAILS if:**
- Application crashes
- Bug still exists (can add courses before target)
- Can exceed target credits
- GPA calculation is wrong
- UI is broken or unresponsive

---

**Ready to test!** Follow Step 1-3 above to transfer and test on another device.
