"""Checks the rebuilt unsigned release, independently disassembled after assembly."""
import unittest, re, json, hashlib, zipfile
from pathlib import Path
import xml.etree.ElementTree as ET

ROOT=Path(__file__).resolve().parents[3]
DEC=ROOT/'verified-revision'
ORIGINAL=ROOT/'original-bytecode'
APK=ROOT/'outputs/unityplayTV-Mobile-1.0.3-r2-unsigned.apk'
A='{http://schemas.android.com/apk/res/android}'

class MobileRevision(unittest.TestCase):
    def test_identity_is_the_same_copy(self):
        root=ET.parse(DEC/'AndroidManifest.xml').getroot()
        self.assertEqual(root.get('package'),'com.unityplaytv.mobile')
        strings={s.get('name'):s.text for s in ET.parse(DEC/'res/values/strings.xml').getroot()}
        self.assertEqual(strings['app_name'],'unityplayTV')
    def test_http_internet_and_existing_components(self):
        root=ET.parse(DEC/'AndroidManifest.xml').getroot()
        self.assertEqual(root.find('application').get(A+'usesCleartextTraffic'),'true')
        self.assertIn('android.permission.INTERNET',[p.get(A+'name') for p in root.findall('uses-permission')])
        self.assertEqual(root.find('application').get(A+'name'),'tv.own.owntv.mobile.OwnTVMobileApp')
    def test_exact_requested_accent_in_both_default_modes(self):
        text=(DEC/'smali/ts5.smali').read_text()
        # Both legacy default teal and violet presets were branded in the first copy.
        self.assertEqual(text.count('0xffa446d2L'),4)
        self.assertNotIn('0xffc9a8e0L',text)
        self.assertNotIn('0xff704487L',text)
        self.assertNotIn('0xff301a40L',text)
    def test_similar_purple_secondary(self):
        text=(DEC/'smali/u15.smali').read_text()
        self.assertIn('0xffd69aecL',text)
        self.assertNotIn('0xfff2b8c6L',text)
    def test_non_array_response_returns_incomplete_in_release(self):
        text=(DEC/'smali/ok9.smali').read_text()
        method=text.split('.method public final t(Ljava/lang/String;Ljava/io/InputStream;Lf03;Lfb1;)')[1].split('.end method')[0]
        start=method.index('Landroid/util/JsonReader;->skipValue()V')
        branch=method[start:method.index('return-object v0',start)]
        self.assertIn('Ljava/lang/Boolean;->FALSE:',branch)
        self.assertNotIn('Ljava/lang/Boolean;->TRUE:',branch)
        # Successful complete arrays still return true elsewhere in the same method.
        self.assertIn('Ljava/lang/Boolean;->TRUE:',method)
    def test_native_libraries_are_bit_identical(self):
        with zipfile.ZipFile(ROOT/'inputs/unityplayTV-Mobile-1.0.3.apk') as old,zipfile.ZipFile(APK) as new:
            libs=[n for n in old.namelist() if n.startswith('lib/') and n.endswith('.so')]
            self.assertEqual(len(libs),30)
            for name in libs:self.assertEqual(hashlib.sha256(old.read(name)).digest(),hashlib.sha256(new.read(name)).digest(),name)
    def test_zip_and_png_integrity(self):
        with zipfile.ZipFile(APK) as z:
            self.assertIsNone(z.testzip())
            for name in z.namelist():
                if name.endswith('.png'):self.assertTrue(z.read(name).startswith(b'\x89PNG\r\n\x1a\n'),name)
    def test_only_reviewed_classes_changed(self):
        approved={r['file'] for r in json.loads((ROOT/'tools/revision-changes.json').read_text())}|{'smali/ok9.smali'}
        before={str(p.relative_to(ORIGINAL)):p for p in ORIGINAL.glob('smali*/**/*.smali')}
        after={str(p.relative_to(DEC)):p for p in DEC.glob('smali*/**/*.smali')}
        self.assertEqual(set(before),set(after))
        changed={name for name,p in before.items() if p.read_text()!=after[name].read_text()}
        self.assertEqual(changed,approved)

if __name__=='__main__':unittest.main(verbosity=2)
