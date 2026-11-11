import re, unittest, json, hashlib
from pathlib import Path
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[2]
CORE = ROOT / 'OwnTV_Core/core/src/main'
TV = ROOT / 'OwnTV/app/src/main'
MOBILE = ROOT / 'OwnTV_Mobile/app/src/main'
PALETTE = (CORE / 'java/tv/own/owntv/core/theme/Palette.kt').read_text(encoding='utf-8')
ANDROID = '{http://schemas.android.com/apk/res/android}'

def token(name):
    return int(re.search(r'const val ' + name + r' = 0x([0-9A-Fa-f]+)L', PALETTE).group(1), 16)

def luminance(color):
    channels = [((color >> shift) & 255) / 255 for shift in (16,8,0)]
    channels = [v/12.92 if v <= .04045 else ((v+.055)/1.055)**2.4 for v in channels]
    return sum(a*b for a,b in zip(channels,(.2126,.7152,.0722)))

def contrast(a,b):
    x,y = sorted((luminance(a),luminance(b)))
    return (y+.05)/(x+.05)

class BrandContract(unittest.TestCase):
    def test_launcher_and_wordmark_names(self):
        tree=ET.parse(CORE/'res/values/donottranslate.xml')
        strings={x.attrib['name']:x.text for x in tree.getroot().findall('string')}
        self.assertEqual(strings['app_name'],'unityplayTV')
        self.assertEqual(strings['brand_own']+strings['brand_tv'],'unityplayTV')
        for root in (TV,MOBILE):
            app=ET.parse(root/'AndroidManifest.xml').getroot().find('application')
            self.assertEqual(app.attrib[ANDROID+'label'],'@string/app_name')
    def test_two_apps_use_branded_local_core(self):
        for project in ('OwnTV','OwnTV_Mobile'):
            props=(ROOT/project/'gradle.properties').read_text(encoding='utf-8')
            self.assertIn('owntv.corePath=../OwnTV_Core',props)
            settings=(ROOT/project/'settings.gradle.kts').read_text(encoding='utf-8')
            self.assertIn('owntv.corePath',settings)
            self.assertIn('includeBuild',settings)
    def test_http_and_internet_remain_enabled(self):
        for root in (TV,MOBILE):
            app=ET.parse(root/'AndroidManifest.xml').getroot().find('application')
            self.assertEqual(app.attrib[ANDROID+'usesCleartextTraffic'],'true')
        permissions=[x.attrib.get(ANDROID+'name') for x in ET.parse(CORE/'AndroidManifest.xml').getroot().findall('uses-permission')]
        self.assertIn('android.permission.INTERNET',permissions)
    def test_both_color_modes_have_accessible_labels(self):
        for mode in ('Dark','Light'):
            for role in ('Surface','Secondary','Tertiary'):
                self.assertGreaterEqual(contrast(token(mode+role),token(mode+'On'+role)),4.5)
            self.assertGreaterEqual(contrast(token(mode+'Background'),token(mode+'OnSurface')),7)
        violet=PALETTE.split('private val VioletPalette = AccentPalette(',1)[1].split('private val GreenPalette',1)[0]
        colors=[int(v,16) for v in re.findall(r'0x([0-9A-Fa-f]+)L',violet)]
        self.assertEqual(colors[0],0xFFA446D2)
        for i in (0,4):
            self.assertGreaterEqual(contrast(colors[i],colors[i+1]),4.5)
            self.assertGreaterEqual(contrast(colors[i+2],colors[i+3]),4.5)
        self.assertEqual(token('DarkSecondary'),0xFFD69AEC)
    def test_no_color_flash_to_old_default(self):
        settings=(CORE/'java/tv/own/owntv/core/settings/SettingsRepository.kt').read_text(encoding='utf-8')
        self.assertIn('?: AccentColor.VIOLET',settings)
        theme=(MOBILE/'java/tv/own/owntv/mobile/ui/theme/MobileTheme.kt').read_text(encoding='utf-8')
        self.assertNotIn('AccentColor.TEAL',theme)
        for root in (TV,MOBILE):
            colors={x.attrib['name']:x.text for x in ET.parse(root/'res/values/colors.xml').getroot().findall('color')}
            self.assertEqual(colors['owntv_window_bg'].upper(),'#100B18')
    def test_all_resource_xml_is_well_formed(self):
        for project in ('OwnTV','OwnTV_Core','OwnTV_Mobile'):
            for f in (ROOT/project).rglob('*.xml'):
                if 'build' not in f.parts: ET.parse(f)
    def test_transport_and_login_match_reviewed_baseline(self):
        reference=json.loads(Path(__file__).with_name('upstream_transport_sha256.json').read_text())
        for rel, expected in reference.items():
            f=ROOT/'OwnTV_Core'/rel
            self.assertEqual(hashlib.sha256(f.read_bytes()).hexdigest(),expected,rel)
    def test_default_tv_banner_is_branded(self):
        for root in (TV,):
            app=ET.parse(root/'AndroidManifest.xml').getroot().find('application')
            self.assertEqual(app.attrib[ANDROID+'banner'],'@drawable/unityplaytv_banner')
        ET.parse(CORE/'res/drawable/unityplaytv_banner.xml')

if __name__=='__main__': unittest.main(verbosity=2)
