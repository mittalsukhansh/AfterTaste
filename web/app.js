/**
 * AfterTaste - Specialty Coffee Journal & Cafe Hopping Companion
 * Core Application Engine & State Store
 */

// --- 1. SEED DATA & INITIAL STATE ---
const INITIAL_BEANS = [
  {
    id: "bean-1",
    roaster: "Sey Coffee",
    roasterCity: "Brooklyn, NY",
    name: "Worka Sakaro",
    origin: "Ethiopia",
    countryCode: "ETH",
    countryFlag: "🇪🇹",
    process: "Washed",
    variety: "Heirloom Variety",
    elevation: "2,050m",
    roastDate: new Date(Date.now() - 10 * 86400000).toISOString().split('T')[0], // 10d rested (Peak window)
    weightTotal: 250,
    weightRemaining: 120,
    tastingNotes: ["Peach Blossom", "Earl Grey", "Apricot"],
    rating: 4.9,
    brewCount: 6,
    status: "ACTIVE",
    notes: "Superb floral jasmine fragrance and tea-like bergamot clarity.",
    photoUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuD0MQvalXuiJGhFVK7hnqXnQ7j7jR3cDKQ55fvymDZAUCVTYPwhytC3tchCWRK-9930uOAhsPdTsZh_eSq10cvQPC0XZ3n_NZ9V7uoCE3vVSIEhPMp8gmAyFQz4x7SmEEXkxbGS8r7M5VfuVyAxAVNhE4VBhhz_USAM5vZn180rRgPdcAxGLeowHDCAcBS4-VXoCTPza5vAZwbLA5ZCS8Xo4447fq_jEncZI05o3kIrrajCPBaV5rOL"
  },
  {
    id: "bean-2",
    roaster: "Manhattan Coffee",
    roasterCity: "Rotterdam",
    name: "Diego Bermudez",
    origin: "Colombia",
    countryCode: "COL",
    countryFlag: "🇨🇴",
    process: "Thermal Shock",
    variety: "Castillo",
    elevation: "1,950m",
    roastDate: new Date(Date.now() - 15 * 86400000).toISOString().split('T')[0], // Degassing complete
    weightTotal: 200,
    weightRemaining: 65,
    tastingNotes: ["Strawberry Candy", "Bubblegum", "Passionfruit"],
    rating: 5.0,
    brewCount: 9,
    status: "ACTIVE",
    notes: "Thermal shock anaerobic processing. Vibrant strawberry sweetness and candy finish.",
    photoUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuB4OW0ks4kWE31a7bUgwqyB33IX8UWubvCXTT-G2XyghJh-JiSb7ujONwPTiM9yzh4njCpcW65lfhbQWOQ0h_95Y0yKCiGis10vk1jQPT_Gxm8D759M9DgwyFaYD3OUjFcqTj0rDKniyPsU5y5j5SOpXWm38xRPsqzok7jPi_a-KIjwrTM2S58c9HEipsu-11r05Dkn1NKZGTMdxH4yCtyv_Lk1PCDoOfiDQhkbogqDuDZRXtsFGH72"
  },
  {
    id: "bean-3",
    roaster: "April Coffee",
    roasterCity: "Copenhagen",
    name: "Volcán Azul",
    origin: "Costa Rica",
    countryCode: "CRI",
    countryFlag: "🇨🇷",
    process: "Natural",
    variety: "Caturra",
    elevation: "1,600m",
    roastDate: new Date(Date.now() - 4 * 86400000).toISOString().split('T')[0], // Opening today
    weightTotal: 250,
    weightRemaining: 250,
    tastingNotes: ["Cacao Nibs", "Blood Orange", "Wild Berry"],
    rating: 4.8,
    brewCount: 1,
    status: "ACTIVE",
    notes: "Ready for initial grind. Crisp blood orange with dark chocolate undertones.",
    photoUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuDTb8tjCfNHNeZXZhTJ71nmaHgYWhR3uaWMkoJyz1Skwo0w0NjGAKAQpxiatn6ym29OC8nerE-QIarf4gODBh6sq8-dP7FLY9X11iPXgDAIzmz8jS08Rg9kyDnhbbjB31OCRYeBA5QqrfFJa_xVqTeg8JSNRXy9gNAiA1u8Re2T3_SqIdE4UVvZZb5CAMzfPN3InAnwBdwC787Chb6AV3KMqB3Fh0-5_iIsiPggaXYwU9-61aAVMxDb"
  },
  {
    id: "bean-4",
    roaster: "Onyx Coffee Lab",
    name: "Ethiopia Gesha Village",
    origin: "Ethiopia",
    countryFlag: "🇪🇹",
    process: "Honey",
    variety: "Gesha 1931",
    elevation: "2,050m",
    roastDate: new Date(Date.now() - 35 * 86400000).toISOString().split('T')[0],
    weightTotal: 250,
    weightRemaining: 0,
    tastingNotes: ["White Flowers", "Papaya", "Starfruit", "Bergamot"],
    status: "FINISHED",
    notes: "Finished last week. Unbelievable fragrance.",
    photoUrl: "https://images.unsplash.com/photo-1498804103079-a6351b050096?auto=format&fit=crop&w=600&q=80"
  },
  {
    id: "bean-5",
    roaster: "Coffee Collective",
    name: "Finca Vista Hermosa",
    origin: "Guatemala",
    countryFlag: "🇬🇹",
    process: "Washed",
    variety: "Bourbon / Catuai",
    elevation: "1,700m",
    roastDate: new Date().toISOString().split('T')[0],
    weightTotal: 250,
    weightRemaining: 250,
    tastingNotes: ["Sweet Clementine", "Milk Chocolate", "Nougat"],
    status: "WISHLIST",
    notes: "Planned for next roastery order.",
    photoUrl: "https://images.unsplash.com/photo-1559525839-8f8373b4d45d?auto=format&fit=crop&w=600&q=80"
  }
];

const INITIAL_BREWS = [
  {
    id: "brew-1",
    cafeName: "Prolog Coffee Bar",
    roaster: "Sey Coffee Roasters",
    beanName: "Colombia El Paraiso",
    method: "Pour Over",
    methodBadge: "Double Anaerobic",
    ratio: "Diego Bermudez Lot #42 • Cauca, 1,930m",
    price: "₹340",
    roastProfile: "Light Scand.",
    temperature: "Hot",
    vessel: "Pottery Cup",
    tastingNotes: ["Bergamot", "Lychee", "Candied Peach", "Jasmine"],
    rating: 4.9,
    notes: "Double Anaerobic. Acidity 86%, Sweetness 80%, Body 65%. Light Scandinavian roast.",
    photoUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuBnzWExKNh0t0oVr3fNBirg5tZqlQ5RPjAYi3Bq6mTShxbrXcIUS_tQ2pxR51nhVf5El4kZ-E4G8JD6CNdZWSZB-cHnG5x-SSsDRne_YsTKeazUTGnp39-37DgVvoXcjLbBxHOEnFlY8XahfV-xVIgXdniPcRpeEO03cc4jepCU8_iqu11EFt_7WAaE3C7Gt_LMBD9quuxajGs-nm_Ra8EVjVJcJ9Qxyzy5tictrffqpc9GyU8XcXhx",
    timestamp: new Date().toISOString()
  },
  {
    id: "brew-2",
    cafeName: "Onyx Espresso Bar",
    roaster: "Onyx Coffee Lab",
    beanName: "Flat White • Monarch Blend",
    method: "Espresso",
    methodBadge: "Espresso",
    ratio: "In 18.0g : Out 38.0g • 27s",
    price: "₹280",
    temperature: "Hot",
    vessel: "Ceramic Cappuccino Cup",
    tastingNotes: ["Dark Chocolate", "Molasses", "Heavy Body"],
    rating: 4.7,
    notes: "Glossy microfoam rosette tulip with rich cocoa notes and velvety sweetness.",
    photoUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuAMhw4f8HiRsrb4F9yME-ImMbJAWC01HMdiRuPjJiGvRHssR67o86DccqhhxMm44IbbVU1o0X8N5tCtB72pVGGtw6tn4wcXzuIr_Dtuxg1Je47k9HwICfOIU64rvRIZSgkY74Joo-WJryqHFtmLyg2P3EHyBVIXHOprE_PlUE33n84Bqi5DJwZ9NtObcB14SGSWXBRleJU6j4XGE2YuVe4H5tlbbzAw1hYRvUKg8vbLsCj-tFC76GCW",
    timeDisplay: "Yesterday • 02:15 PM",
    timestamp: new Date(Date.now() - 86400000 * 1).toISOString()
  },
  {
    id: "brew-3",
    cafeName: "Cub Coffee Bar",
    roaster: "Tim Wendelboe",
    beanName: "Caballero Geisha • Washed",
    method: "Pour Over",
    methodBadge: "Filter Chemex",
    ratio: "1:16 Ratio • 30g / 480ml • 3:40",
    price: "₹350",
    temperature: "Hot",
    vessel: "Classic Chemex Collar",
    tastingNotes: ["Red Apple", "Caramel", "Crisp Acidity"],
    rating: 4.5,
    notes: "Clear sparkling amber filter coffee with crisp apple malic acidity and clean honey sweetness.",
    photoUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuCZkbxyHRxy-v1C-fxtuRgJmLe3m36PzPjX8SqY57kn9Gv5i7bpp2Lpg6JTDR3T3Pg6Rw09GdyEB2-LMNbyGeH3vRFnjlX0CfJP3fE1w-Tb8fz2HPLI-4hZW8zMF8ToH_0jHggwDc0xWVlmgajBJSuzbB5YirjbZxtXU2tUOScDiXtRsmSqR2J6PywBex981WqCUHpDEAHs0oWwGX5un3eD6PfMe3l86qno9K12OdiBM-Y5baWHR-yY",
    timeDisplay: "Oct 12 • 09:10 AM",
    timestamp: new Date(Date.now() - 86400000 * 2).toISOString()
  },
  {
    id: "brew-4",
    cafeName: "April Coffee Store",
    roaster: "April Coffee Roasters",
    beanName: "Kenya Kamwangi AA",
    method: "AeroPress",
    methodBadge: "AeroPress",
    ratio: "Inverted • 18g / 200ml • 2:15",
    price: "₹320",
    temperature: "Hot",
    vessel: "April Ceramic Cup",
    tastingNotes: ["Blackcurrant", "Red Grapefruit", "Bergamot"],
    rating: 4.8,
    notes: "Intense berry aroma and vibrant citrus finish.",
    photoUrl: "https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=600&q=80",
    timeDisplay: "Oct 10 • 11:30 AM",
    timestamp: new Date(Date.now() - 86400000 * 4).toISOString()
  },
  {
    id: "brew-5",
    cafeName: "Coffee Collective Bernikow",
    roaster: "Coffee Collective",
    beanName: "Takesi Geisha • Cold Drip",
    method: "Cold Brew",
    methodBadge: "Cold Brew",
    ratio: "12h Slow Drip • Single Origin",
    price: "₹300",
    temperature: "Iced",
    vessel: "Nordic Tumbler",
    tastingNotes: ["Jasmine", "White Peach", "Sparkling Bergamot"],
    rating: 4.9,
    notes: "Refreshing floral tea-like clarity.",
    photoUrl: "https://images.unsplash.com/photo-1510591509098-f4fdc6d0ff04?auto=format&fit=crop&w=600&q=80",
    timeDisplay: "Oct 08 • 03:45 PM",
    timestamp: new Date(Date.now() - 86400000 * 6).toISOString()
  }
];

const INITIAL_CAFES = [
  {
    id: "cafe-1",
    name: "Prolog Coffee Bar",
    neighborhood: "Kødbyen, Høkerboderne 9",
    distance: "0.4 km",
    lat: 55.6698,
    lng: 12.5594,
    coords: { x: 24, y: 28 },
    visitedStatus: "VISITED",
    statusText: "Dined In • Yesterday",
    subBadge: "Roastery & Bar",
    userRating: 4.9,
    communityRating: 4.9,
    reviewsCount: "340 rev",
    hours: "Open until 18:00",
    photoUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuCTXrNgLb_1bRkAYoc91TF9LhCZJ4BH8IJ4VW14m__XfAFYX9S83fcs3mooa-FRcdxLkyakofSULNxoLnFOS5mojTYTyyucMmRNxg4H3lX12tFb9fn86URRPQb0xC-JnDxeccpIB4T4NnhBRtySsSo6VpT-NmLDf4CW6dYZf6FJgDVonWqa2VBRFCMi3AHdw9MqdROY8SvyfuDg-N_mMlSjAHlLL0MZy_MWeEwm1A7L_bh1SJm2TmlZ",
    tastedItems: [
      { 
        name: "Hand Drip: Kenya Kamwangi", 
        badge: "AA Washed",
        notes: "Sensory: Blackcurrant acidity, silky body, bergamot finish", 
        price: "₹340", 
        rating: 5.0 
      },
      { 
        name: "Cardamom Bun (Pastry)", 
        badge: "Fresh Bake",
        notes: "Buttery, crushed pod aroma, caramelized bottom", 
        price: "₹220", 
        rating: 4.8 
      }
    ],
    liveMenu: [
      { category: "Filter", item: "Ethiopia Shantawene", price: "₹300", notes: "Natural • Peach & Jasmine floral notes" },
      { category: "Filter", item: "Batch Brew Nordic", price: "₹240", notes: "Colombian Pink Bourbon rotative" },
      { category: "Espresso Bar", item: "Double Espresso (House Roast)", price: "₹200", notes: "Sweet clementine and dark chocolate" },
      { category: "Bakes", item: "Cardamom Twist", price: "₹220", notes: "Cardamom, sourdough, brown butter" }
    ]
  },
  {
    id: "cafe-2",
    name: "April Coffee Store",
    neighborhood: "Ryesgade 86, Østerbro",
    distance: "1.8 km",
    lat: 55.6946,
    lng: 12.5746,
    coords: { x: 72, y: 34 },
    visitedStatus: "VISITED",
    statusText: "Dined In • 3 Visits logged",
    subBadge: "All-Time Favorite",
    userRating: 5.0,
    communityRating: 5.0,
    reviewsCount: "Personal",
    hours: "Open until 17:30",
    photoUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuAqQtCbFaUH_0g9WA4Q2I5Ieon3o7raqfAV-U5Hh8qJRCEdLhvvRl3_ZLZUweYfAhYCv8t987tMSKlZmJBZfqQc06iZdf2S74NuBrzifC6489P265rU_fOYqq1U6dURMJgpD595pgTTJrIMxB6cedocq-kmeh9aD63VLbG70ug_ZxZEKMxpQGw3ifvmRNgZ_Hn08Uw6WymQRgffILD2dPyYbitM0f6aV5cBxBxej0SOdasvFC9yxvX4",
    tastedItems: [
      { 
        name: "Volcán Azul Geisha Lot 12", 
        badge: "Anaerobic",
        notes: "Brew Method: April Plastic Brewer • Bergamot, passionfruit nectar", 
        price: "₹450", 
        rating: 5.0 
      },
      { 
        name: "Canelé de Bordeaux", 
        badge: "Classic Bake",
        notes: "Crisp beeswax crust, rum & vanilla bean custard", 
        price: "₹220", 
        rating: 4.9 
      }
    ],
    signatureBar: "Aeropress Inverted & April Brewer Single Serves",
    liveMenu: [
      { category: "April Brewer Lot", item: "Kenya AA Single Origin", price: "₹360", notes: "Blackcurrant & cane sugar" },
      { category: "April Brewer Lot", item: "Panama Geisha Natural", price: "₹550", notes: "Jasmine floral & mango" },
      { category: "Espresso", item: "Flat White (Oat)", price: "₹280", notes: "Volcán Azul espresso blend" }
    ]
  },
  {
    id: "cafe-3",
    name: "Coffee Collective Bernikow",
    neighborhood: "Kristen Bernikows Gade 2",
    distance: "2.1 km",
    lat: 55.6798,
    lng: 12.5815,
    coords: { x: 60, y: 72 },
    visitedStatus: "WISHLIST",
    statusText: "Wishlist • Not Yet Visited",
    subBadge: "Iconic Spot",
    userRating: null,
    communityRating: 4.7,
    reviewsCount: "1.2k rev",
    hours: "Open until 19:00",
    photoUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuCSyL119qKW3JyThDsVpSotzLLFqwhmDRtyydZHMkIsAYUxgUNc-DtyeFelqSCyF6iT-PrDukq07M7ImqvF7nKMum2JNn2NxL0V2h4qSsiJunCxyHwhUk9e1jx-dx0KERoP8Se3kooO0p0J0dormZeE2m1bSd1L1X48CRsiIdH_E6kBwQ61TnWftGBcdHzl6z8Fh-XKvi1u8-EQEXO8X_v3Ng2vQaytWo2OlLcfEtjNJ-hz-1KWywpl",
    communityRenowned: "Direct Trade Pioneer • Celebrated for single estate Bolivian Takesi Geisha.",
    fetchedMenu: [
      { item: "Kieni Espresso", price: "₹240", tag: "Blackcurrant" },
      { item: "Karatina Chemex", price: "₹340", tag: "Crisp Citrus" },
      { item: "Cascara Kombucha", price: "₹260", tag: "Sparkling Tea" }
    ],
    tastedItems: [],
    liveMenu: [
      { category: "Pour Over", item: "Kieni, Kenya (Washed)", price: "₹320", notes: "Intense blackcurrant & rhubarb" },
      { category: "Espresso", item: "Takesi Geisha, Bolivia", price: "₹290", notes: "Bergamot & white peach" },
      { category: "Soft Serve", item: "Coffee Soft Ice with Crisp", price: "₹280", notes: "Infused with cold drip concentrate" }
    ]
  },
  {
    id: "cafe-4",
    name: "Blue Tokai Coffee Roasters",
    neighborhood: "Saidulajab, Saket",
    distance: "2.5 km",
    lat: 28.5204,
    lng: 77.2005,
    coords: { x: 40, y: 45 },
    visitedStatus: "VISITED",
    statusText: "Dined In • Weekly Stop",
    subBadge: "Microlot Roastery",
    userRating: 4.9,
    communityRating: 4.8,
    reviewsCount: "2.4k rev",
    hours: "Open until 22:00",
    photoUrl: "https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&w=600&q=80",
    tastedItems: [
      { name: "Attikan Estate Pour Over", badge: "Light Medium", notes: "Sensory: Dark chocolate, sweet fig, mild acidity", price: "₹260", rating: 4.8 },
      { name: "Cortado", badge: "Double Ristretto", notes: "Rich crema with dense textured microfoam", price: "₹220", rating: 4.7 }
    ],
    liveMenu: [
      { category: "Manual Brew", item: "Riverdale Estate N72", price: "₹290", notes: "Watermelon, rose water & candy sweetness" },
      { category: "Manual Brew", item: "Kalledevarapura Pulp Sun Dried", price: "₹270", notes: "Orange blossom, hazelnut & milk chocolate" },
      { category: "Espresso", item: "Vienna Roast Flat White", price: "₹240", notes: "Full body, cocoa nibs" }
    ]
  }
];

// --- 2. MULTILINGUAL DICTIONARY (i18n) ---
const I18N = {
  en: {
    appTitle: "AfterTaste",
    morningDialIn: "Morning Dial-in",
    afternoonRitual: "Afternoon Ritual",
    eveningSip: "Evening Tasting",
    greeting: "Good morning, Maya ☕",
    greetingSub: "Ready for today's cafe hop or morning brew ritual?",
    weeklyRhythm: "Weekly Rhythm",
    brewsCount: "Brews",
    vaultFavorite: "Vault Favorite",
    extractionScore: "Avg Score",
    quickLogTitle: "Quick Log Today's Cup",
    quickLogSub: "Cafe, drink, sensory notes, and cup photo in 20 seconds.",
    initiateLog: "Initiate Brew Log",
    featuredTitle: "Today's Featured Cup",
    editRecipe: "View Details",
    journalFeed: "Journal History",
    filterAll: "All",
    filterPourOver: "Pour Over",
    filterEspresso: "Espresso",
    filterColdBrew: "Cold Brew",
    filterBatchBrew: "Batch Brew",
    navJournal: "Journal",
    navVault: "Vault",
    navAnalytics: "Analytics",
    navCafes: "Cafes",
    scanBagTitle: "Scan New Bag",
    scanBagSub: "Instant roast date, origin & tasting note logging",
    activeBags: "Active Bags",
    finishedBags: "Finished",
    wishlistBags: "Wishlist",
    currentlyDialing: "Currently Dialing In",
    sensoryEquilibrium: "Flavor Equilibrium",
    sensorySub: "5-Axis Palate Radar",
    dominantAromas: "Dominant Aroma Frequency",
    terroirsTitle: "Terroir Exploration Archive",
    cafesExplored: "Cafes Explored",
    drinksTasted: "Drinks Tasted",
    avgRating: "Avg Rating",
    saveBrew: "Save to Coffee Journal"
  },
  da: {
    appTitle: "AfterTaste",
    morningDialIn: "Morgen Rutine",
    afternoonRitual: "Eftermiddags Kaffe",
    eveningSip: "Aften Smagning",
    greeting: "Godmorgen, Maya ☕",
    greetingSub: "Klar til dagens café-besøg eller håndbryg?",
    weeklyRhythm: "Ugentlig Rytme",
    brewsCount: "Bryg",
    vaultFavorite: "Favorit Bønne",
    extractionScore: "Snit Score",
    quickLogTitle: "Hurtig Log Dagens Kop",
    quickLogSub: "Café, drik, smagsnoter og foto på 20 sekunder.",
    initiateLog: "Start Kaffelog",
    featuredTitle: "Dagens Udvalgte Kop",
    editRecipe: "Se Detaljer",
    journalFeed: "Kaffejournal",
    filterAll: "Alle",
    filterPourOver: "Pour Over",
    filterEspresso: "Espresso",
    filterColdBrew: "Cold Brew",
    filterBatchBrew: "Batch Drip",
    navJournal: "Journal",
    navVault: "Bønneboks",
    navAnalytics: "Analyse",
    navCafes: "Caféer",
    scanBagTitle: "Scan Kaffepose",
    scanBagSub: "AI udtrækning af ristedato, oprindelse & smag",
    activeBags: "Aktive Poser",
    finishedBags: "Tømte",
    wishlistBags: "Ønskeliste",
    currentlyDialing: "I Kværnen Nu",
    sensoryEquilibrium: "Smagsbalance Radar",
    sensorySub: "5-Aksers Palet Radar",
    dominantAromas: "Dominante Aromafrekvenser",
    terroirsTitle: "Oprindelses Arkiv",
    cafesExplored: "Caféer Besøgt",
    drinksTasted: "Drikke Smagt",
    avgRating: "Gns. Karakter",
    saveBrew: "Gem i Kaffejournal"
  },
  ja: {
    appTitle: "AfterTaste",
    morningDialIn: "モーニング ダイアルイン",
    afternoonRitual: "午後のコーヒー儀式",
    eveningSip: "イブニング カッピング",
    greeting: "おはよう、マヤ ☕",
    greetingSub: "今日のカフェ巡りやハンドドリップの準備はできましたか？",
    weeklyRhythm: "今週のリズム",
    brewsCount: "杯",
    vaultFavorite: "お気に入り豆",
    extractionScore: "平均評価",
    quickLogTitle: "今日の一杯をクイック記録",
    quickLogSub: "カフェ、豆、フレーバーを20秒で記録。",
    initiateLog: "ログを開始する",
    featuredTitle: "今日のおすすめカップ",
    editRecipe: "詳細を見る",
    journalFeed: "ブリュー ジャーナル",
    filterAll: "すべて",
    filterPourOver: "ドリップ",
    filterEspresso: "エスプレッソ",
    filterColdBrew: "コールドブリュー",
    filterBatchBrew: "バッチ抽出",
    navJournal: "ジャーナル",
    navVault: "ビーン倉庫",
    navAnalytics: "味覚分析",
    navCafes: "カフェ",
    scanBagTitle: "豆袋をAIスキャン",
    scanBagSub: "焙煎日、産地、ノートを自動抽出",
    activeBags: "使用中の豆",
    finishedBags: "飲み干した豆",
    wishlistBags: "ウィッシュリスト",
    currentlyDialing: "現在グラインド中",
    sensoryEquilibrium: "フレーバー バランス",
    sensorySub: "5軸 味覚レーダー",
    dominantAromas: "頻出アロマ分析",
    terroirsTitle: "産地テロワール記録",
    cafesExplored: "訪問カフェ数",
    drinksTasted: "テイスティング数",
    avgRating: "平均スコア",
    saveBrew: "ジャーナルに保存"
  },
  fr: {
    appTitle: "AfterTaste",
    morningDialIn: "Rituel du Matin",
    afternoonRitual: "Pause de l'Après-midi",
    eveningSip: "Dégustation du Soir",
    greeting: "Bonjour, Maya ☕",
    greetingSub: "Prête pour une tournée des cafés ou un pour-over maison ?",
    weeklyRhythm: "Rythme Hebdo",
    brewsCount: "Tasses",
    vaultFavorite: "Grain Favori",
    extractionScore: "Note Moyenne",
    quickLogTitle: "Enregistrer la Tasse",
    quickLogSub: "Café, boisson, notes aromatiques et photo en 20s.",
    initiateLog: "Lancer l'Enregistrement",
    featuredTitle: "Tasse à l'Honneur",
    editRecipe: "Voir Détails",
    journalFeed: "Journal des Tasses",
    filterAll: "Tous",
    filterPourOver: "Pour Over",
    filterEspresso: "Espresso",
    filterColdBrew: "Cold Brew",
    filterBatchBrew: "Filtre",
    navJournal: "Journal",
    navVault: "Cave à Grains",
    navAnalytics: "Analytique",
    navCafes: "Cafés",
    scanBagTitle: "Scanner le Sachet",
    scanBagSub: "Extraction IA date de torréfaction & notes",
    activeBags: "Sachets Actifs",
    finishedBags: "Terminés",
    wishlistBags: "Envies",
    currentlyDialing: "En Dégustation",
    sensoryEquilibrium: "Équilibre Sensoriel",
    sensorySub: "Radar du Palais 5-Axes",
    dominantAromas: "Fréquence des Arômes",
    terroirsTitle: "Archive des Terroirs",
    cafesExplored: "Cafés Visités",
    drinksTasted: "Boissons Goûtées",
    avgRating: "Note Moy.",
    saveBrew: "Enregistrer au Journal"
  }
};

// --- 3. STATE STORE & PERSISTENCE ---
class Store {
  constructor() {
    this.beans = this.load('aftertaste_beans', INITIAL_BEANS);
    this.brews = this.load('aftertaste_brews', INITIAL_BREWS);
    this.cafes = this.load('aftertaste_cafes', INITIAL_CAFES);
    this.theme = localStorage.getItem('aftertaste_theme') || 'light';
    this.lang = localStorage.getItem('aftertaste_lang') || 'en';
    this.activeTab = 'journal';
    this.journalFilter = 'All';
    this.vaultSegment = 'ACTIVE';
    this.cafeFilter = 'ALL';
    this.neighborhood = 'Copenhagen, Vesterbro';
  }

  load(key, fallback) {
    try {
      const data = localStorage.getItem(key);
      return data ? JSON.parse(data) : fallback;
    } catch (e) {
      console.warn("Storage load error:", e);
      return fallback;
    }
  }

  save(key, val) {
    try {
      localStorage.setItem(key, JSON.stringify(val));
    } catch (e) {
      console.warn("Storage save error:", e);
    }
  }

  setTheme(theme) {
    this.theme = theme;
    localStorage.setItem('aftertaste_theme', theme);
    if (theme === 'dark') {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }

  setLang(lang) {
    this.lang = lang;
    localStorage.setItem('aftertaste_lang', lang);
  }

  addBrew(brew) {
    this.brews.unshift(brew);
    this.save('aftertaste_brews', this.brews);

    // If brew is linked to an active bean, decrement ~15g
    const matchingBean = this.beans.find(b => 
      b.name.toLowerCase().includes(brew.beanName.toLowerCase()) ||
      brew.beanName.toLowerCase().includes(b.name.toLowerCase())
    );
    if (matchingBean && matchingBean.weightRemaining > 0) {
      matchingBean.weightRemaining = Math.max(0, matchingBean.weightRemaining - 15);
      if (matchingBean.weightRemaining === 0) {
        matchingBean.status = "FINISHED";
      }
      this.save('aftertaste_beans', this.beans);
    }
  }

  addBean(bean) {
    this.beans.unshift(bean);
    this.save('aftertaste_beans', this.beans);
  }

  toggleWishlistCafe(cafeId) {
    const cafe = this.cafes.find(c => c.id === cafeId);
    if (cafe) {
      cafe.visitedStatus = cafe.visitedStatus === 'WISHLIST' ? 'NOT_VISITED' : 'WISHLIST';
      this.save('aftertaste_cafes', this.cafes);
    }
  }
}

const store = new Store();

// --- 4. FLAVOR RADAR & ANALYTICS CALCULATIONS ---
function calculatePalateMetrics() {
  const brews = store.brews;
  if (!brews || brews.length === 0) {
    return { acidity: 88, sweetness: 92, body: 70, clarity: 95, bitterness: 25 };
  }

  // Baseline calibration from enthusiast passport (Page 5 design)
  let acidity = 88;
  let sweetness = 92;
  let body = 70;
  let clarity = 95;
  let bitterness = 25;

  brews.forEach(b => {
    const notes = (b.tastingNotes || []).join(' ').toLowerCase();
    if (notes.includes('citric') || notes.includes('citrus') || notes.includes('grapefruit') || notes.includes('apple')) acidity += 1;
    if (notes.includes('strawberry') || notes.includes('peach') || notes.includes('sugar') || notes.includes('honey')) sweetness += 1;
    if (notes.includes('chocolate') || notes.includes('silky') || notes.includes('body') || notes.includes('nougat')) body += 1;
    if (notes.includes('jasmine') || notes.includes('floral') || notes.includes('clean') || notes.includes('bergamot')) clarity += 1;
    if (notes.includes('bitter') || notes.includes('cacao') || notes.includes('dark')) bitterness += 1;

    if (b.method === 'Pour Over') clarity += 1;
    if (b.method === 'Espresso') { body += 1; bitterness += 1; }
  });

  return {
    acidity: Math.min(98, Math.max(30, acidity)),
    sweetness: Math.min(98, Math.max(30, sweetness)),
    body: Math.min(98, Math.max(30, body)),
    clarity: Math.min(98, Math.max(35, clarity)),
    bitterness: Math.min(85, Math.max(15, bitterness))
  };
}

// Calculate days off roast and optimal window
function getBeanDegassingInfo(roastDateStr) {
  const roastDate = new Date(roastDateStr);
  const now = new Date();
  const diffDays = Math.max(0, Math.floor((now - roastDate) / (1000 * 60 * 60 * 24)));

  if (diffDays <= 7) {
    return {
      days: diffDays,
      badgeText: `${diffDays}d • Degassing`,
      badgeClass: "bg-surface-container-high text-on-surface-variant",
      isPeak: false
    };
  } else if (diffDays <= 28) {
    return {
      days: diffDays,
      badgeText: `${diffDays}d rested • Peak Window 🌟`,
      badgeClass: "bg-emerald-100 text-emerald-800 dark:bg-emerald-950 dark:text-emerald-300 font-semibold",
      isPeak: true
    };
  } else {
    return {
      days: diffDays,
      badgeText: `${diffDays}d • Extended Rest`,
      badgeClass: "bg-amber-100 text-amber-800 dark:bg-amber-950 dark:text-amber-300",
      isPeak: false
    };
  }
}

// --- 5. RENDER FUNCTIONS ---

// Update Time Greeting
function updateGreetingHeader() {
  const hour = new Date().getHours();
  const timeBadge = document.getElementById('currentTimeBadge');
  const greetingEl = document.getElementById('greetingTitle');
  const greetingSub = document.getElementById('greetingSubtitle');
  const dict = I18N[store.lang] || I18N.en;

  const now = new Date();
  const timeStr = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  if (timeBadge) timeBadge.textContent = timeStr;

  let greetingText = dict.greeting;
  if (hour >= 12 && hour < 17) {
    greetingText = dict.greeting.replace('Good morning', 'Good afternoon').replace('Godmorgen', 'God eftermiddag');
  } else if (hour >= 17) {
    greetingText = dict.greeting.replace('Good morning', 'Good evening').replace('Godmorgen', 'Godaften');
  }

  if (greetingEl) greetingEl.textContent = greetingText;
  if (greetingSub) greetingSub.textContent = dict.greetingSub;
}

// Render Daily Brew Journal Stream (Page 3 Journal History)
function renderJournalScreen() {
  const container = document.getElementById('journalFeedContainer');
  if (!container) return;

  const filter = store.journalFilter;
  let list = store.brews;
  if (filter !== 'All') {
    list = list.filter(b => b.method === filter || b.methodBadge === filter);
  }

  // Update total rhythm counter
  const weeklyRhythmCount = document.getElementById('weeklyRhythmCount');
  if (weeklyRhythmCount) weeklyRhythmCount.textContent = `3 Brews`;

  if (list.length === 0) {
    container.innerHTML = `
      <div class="card-nordic p-8 text-center flex flex-col items-center justify-center my-4">
        <span class="material-symbols-outlined text-4xl text-secondary mb-2">coffee</span>
        <h4 class="font-serif text-lg font-bold text-primary">No ${filter} brews recorded yet</h4>
        <p class="text-sm text-on-surface-variant mt-1">Tap the '+' button below to log your morning cup!</p>
      </div>
    `;
    return;
  }

  container.innerHTML = list.map(b => {
    const timeDisplay = b.timeDisplay || new Date(b.timestamp).toLocaleDateString([], {
      month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit'
    });

    let badgeClass = "bg-surface-container text-on-surface";
    const badgeText = b.methodBadge || b.method;
    if (b.method === 'Espresso') {
      badgeClass = "bg-[#ffdbc9] dark:bg-[#753400] text-[#753400] dark:text-[#ffdbc9]";
    } else if (b.method === 'Pour Over' || badgeText.includes('Chemex') || badgeText.includes('Anaerobic')) {
      badgeClass = "bg-[#cbebc3] dark:bg-[#334d2f] text-[#334d2f] dark:text-[#cbebc3]";
    } else if (b.method === 'Cold Brew') {
      badgeClass = "bg-surface-container-high text-primary";
    } else if (b.method === 'AeroPress') {
      badgeClass = "bg-secondary-container text-on-secondary-container";
    }

    return `
      <div class="bg-surface-container-low rounded-xl p-3.5 shadow-sm flex flex-col gap-2.5 border border-black/[0.04] dark:border-white/[0.05] transition-all hover:bg-surface-container">
        <!-- Top: Coffee Details & Rating -->
        <div class="flex items-start justify-between gap-2">
          <div class="min-w-0 flex-1">
            <span class="text-[11px] font-semibold text-secondary uppercase tracking-wider block">${b.roaster}</span>
            <h4 class="font-serif text-sm font-bold text-primary truncate mt-0.5">${b.beanName}</h4>
            <p class="text-xs text-on-surface-variant mt-0.5">${badgeText} • ${b.ratio} • ${timeDisplay}</p>
          </div>
          <div class="flex items-center gap-2 flex-shrink-0">
            <div class="flex items-center gap-0.5 text-secondary">
              <span class="material-symbols-outlined text-[13px] fill-1">star</span>
              <span class="text-xs font-bold text-primary">${b.rating.toFixed(1)}</span>
            </div>
            <div class="w-11 h-11 rounded-lg overflow-hidden flex-shrink-0 bg-surface-container shadow-sm">
              <img class="w-full h-full object-cover" src="${b.photoUrl}" alt="${b.beanName}">
            </div>
          </div>
        </div>

        <!-- Cupping Notes -->
        <div class="flex flex-wrap gap-1.5 pt-0.5">
          ${(b.tastingNotes || []).map(note => `
            <span class="px-2 py-0.5 rounded-full bg-surface text-[10px] text-on-surface-variant font-medium border border-outline-variant/15">
              ${note}
            </span>
          `).join('')}
        </div>
      </div>
    `;
  }).join('');
}

function triggerQuickLogWithToast() {
  const toast = document.getElementById('toastNotification');
  if (toast) {
    toast.classList.remove('translate-y-16', 'opacity-0');
    toast.classList.add('translate-y-0', 'opacity-100');
    setTimeout(() => {
      toast.classList.add('translate-y-16', 'opacity-0');
      toast.classList.remove('translate-y-0', 'opacity-100');
    }, 2400);
  }
  setTimeout(() => {
    openModal('modal-log-brew');
  }, 400);
}

// Render Bean Vault Screen
function renderVaultScreen() {
  const container = document.getElementById('vaultBeansContainer');
  if (!container) return;

  const segment = store.vaultSegment;
  const searchInput = document.getElementById('vaultSearchInput');
  const query = (searchInput ? searchInput.value : '').toLowerCase().trim();

  let list = store.beans.filter(b => b.status === segment);
  if (query) {
    list = list.filter(b => 
      b.name.toLowerCase().includes(query) ||
      b.roaster.toLowerCase().includes(query) ||
      b.origin.toLowerCase().includes(query) ||
      (b.tastingNotes || []).some(n => n.toLowerCase().includes(query))
    );
  }

  // Update counts
  const activeCount = store.beans.filter(b => b.status === 'ACTIVE').length;
  const finishedCount = store.beans.filter(b => b.status === 'FINISHED').length;
  const wishlistCount = store.beans.filter(b => b.status === 'WISHLIST').length;

  const activeBadge = document.getElementById('badgeActiveBeans');
  const finishedBadge = document.getElementById('badgeFinishedBeans');
  const wishlistBadge = document.getElementById('badgeWishlistBeans');
  if (activeBadge) activeBadge.textContent = activeCount;
  if (finishedBadge) finishedBadge.textContent = finishedCount;
  if (wishlistBadge) wishlistBadge.textContent = wishlistCount;

  if (list.length === 0) {
    container.innerHTML = `
      <div class="card-nordic p-8 text-center flex flex-col items-center justify-center my-4">
        <span class="material-symbols-outlined text-4xl text-secondary mb-2">inventory_2</span>
        <h4 class="font-serif text-lg font-bold text-primary">No bags found in ${segment.toLowerCase()}</h4>
        <p class="text-sm text-on-surface-variant mt-1">Scan a bag label with AI OCR or add one manually!</p>
      </div>
    `;
    return;
  }

  container.innerHTML = list.map(b => {
    const degassing = getBeanDegassingInfo(b.roastDate);
    const percentRemaining = Math.round((b.weightRemaining / b.weightTotal) * 100);
    const countryCode = b.countryCode || (b.origin ? b.origin.substring(0, 3).toUpperCase() : 'ETH');
    const isLowInventory = percentRemaining < 35 && percentRemaining > 0;
    const isUnopened = percentRemaining === 100;

    return `
      <article class="bg-surface-container-low rounded-xl p-3.5 shadow-sm border border-black/[0.04] dark:border-white/[0.05] transition-all hover:bg-surface-container flex flex-col gap-2.5">
        <!-- Top: Roaster, Bean & Thumbnail -->
        <div class="flex items-start justify-between gap-3">
          <div class="min-w-0 flex-1">
            <div class="flex items-center gap-1.5 flex-wrap">
              <span class="text-[11px] font-semibold text-secondary uppercase tracking-wider">${b.roaster}</span>
              <span class="text-outline-variant">•</span>
              <span class="text-[11px] text-on-surface-variant">${b.origin}</span>
            </div>
            <h3 class="font-serif text-base font-bold text-primary tracking-tight truncate mt-0.5">${b.name}</h3>
            <p class="text-xs text-on-surface-variant mt-0.5">${b.process} • ${degassing.badgeText}</p>
          </div>
          <div class="w-13 h-15 rounded-lg overflow-hidden flex-shrink-0 bg-surface-container shadow-sm" style="width: 52px; height: 60px;">
            <img src="${b.photoUrl || 'https://images.unsplash.com/photo-1587734195503-904fca47e0e9?auto=format&fit=crop&w=400&q=80'}" 
                 alt="${b.name}" class="w-full h-full object-cover">
          </div>
        </div>

        <!-- Flavor Notes & Weight -->
        <div class="flex items-center justify-between gap-2 pt-0.5">
          <div class="flex flex-wrap gap-1">
            ${(b.tastingNotes || []).slice(0, 3).map(note => `
              <span class="px-2 py-0.5 rounded-full bg-surface text-[10px] text-on-surface-variant font-medium border border-outline-variant/15">
                ${note}
              </span>
            `).join('')}
          </div>
          <span class="text-[11px] text-on-surface-variant font-medium flex-shrink-0">~${b.weightRemaining}g</span>
        </div>

        <!-- Action Row -->
        <div class="flex items-center justify-between pt-1 border-t border-black/[0.03] dark:border-white/[0.04]">
          <div class="flex items-center gap-0.5 text-secondary">
            <span class="material-symbols-outlined text-[13px] fill-1">star</span>
            <span class="text-xs font-bold text-primary">${b.rating || '4.9'}</span>
          </div>
          <div class="flex items-center gap-1.5">
            <button onclick="quickDeductGrams('${b.id}', 15)" class="px-2 py-0.5 rounded text-[11px] font-semibold text-on-surface-variant hover:text-primary transition-colors" title="Deduct 15g single brew">
              -15g
            </button>
            <button onclick="prefillLoggerWithBean('${b.id}')" class="px-3 py-1 rounded-lg bg-primary text-on-primary text-xs font-bold hover:bg-secondary transition-colors">
              Brew
            </button>
          </div>
        </div>
      </article>
    `;
  }).join('');
}

// Render Cafe Map & Dining Passport (Page 2 Dossiers & Cartography)
function renderCafesScreen() {
  const container = document.getElementById('cafesCardsContainer');
  const pinsContainer = document.getElementById('mapPinsContainer');
  if (!container) return;

  const filter = store.cafeFilter || 'ALL';
  const searchInput = document.getElementById('cafeSearchInput');
  const searchTerm = searchInput ? searchInput.value.trim().toLowerCase() : '';

  let list = store.cafes;

  // Filter segment
  if (filter === 'VISITED') {
    list = list.filter(c => c.visitedStatus === 'VISITED');
  } else if (filter === 'WISHLIST') {
    list = list.filter(c => c.visitedStatus === 'WISHLIST');
  } else if (filter === 'SINGLE_ORIGIN') {
    list = list.filter(c => 
      (c.liveMenu && c.liveMenu.some(m => m.notes && m.notes.toLowerCase().includes('single') || m.notes && m.notes.toLowerCase().includes('bourbon') || m.notes && m.notes.toLowerCase().includes('natural'))) ||
      (c.tastedItems && c.tastedItems.some(t => t.name.toLowerCase().includes('kenya') || t.name.toLowerCase().includes('geisha')))
    );
  } else if (filter === 'MATCHA') {
    list = list.filter(c => 
      (c.tastedItems && c.tastedItems.some(t => t.name.toLowerCase().includes('bun') || t.name.toLowerCase().includes('canelé') || t.name.toLowerCase().includes('pastry'))) ||
      (c.liveMenu && c.liveMenu.some(m => m.category.toLowerCase().includes('bake') || m.item.toLowerCase().includes('bun') || m.item.toLowerCase().includes('croissant')))
    );
  }

  // Text search
  if (searchTerm) {
    list = list.filter(c => 
      c.name.toLowerCase().includes(searchTerm) ||
      c.neighborhood.toLowerCase().includes(searchTerm) ||
      (c.tastedItems && c.tastedItems.some(t => t.name.toLowerCase().includes(searchTerm) || (t.notes && t.notes.toLowerCase().includes(searchTerm)))) ||
      (c.liveMenu && c.liveMenu.some(m => m.item.toLowerCase().includes(searchTerm) || (m.notes && m.notes.toLowerCase().includes(searchTerm))))
    );
  }

  // High level passport counters
  const exploredCount = store.cafes.filter(c => c.visitedStatus === 'VISITED').length;
  const exploredEl = document.getElementById('passportExploredCount');
  const drinksEl = document.getElementById('passportDrinksCount');
  const ratingEl = document.getElementById('passportRatingCount');
  if (exploredEl) exploredEl.textContent = exploredCount > 2 ? exploredCount : 8;
  if (drinksEl) drinksEl.textContent = 14;
  if (ratingEl) ratingEl.textContent = '4.85';

  // Render Vector Map Interactive Pins
  if (pinsContainer) {
    pinsContainer.innerHTML = store.cafes.map((c) => {
      const isVisited = c.visitedStatus === 'VISITED';
      const isApril = c.id === 'cafe-2';
      const isProlog = c.id === 'cafe-1';
      const shortName = isProlog ? 'Prolog' : (isApril ? 'April' : 'Collective');
      const score = c.communityRating || c.userRating || 4.7;

      if (isProlog) {
        return `
          <div class="absolute cursor-pointer -translate-x-1/2 -translate-y-1/2 group z-20 transition-transform hover:scale-110 active:scale-95"
               style="left: ${c.coords.x}%; top: ${c.coords.y}%;"
               onclick="focusCafeCard('${c.id}')"
               title="${c.name}">
            <div class="bg-primary-container text-white px-2.5 py-1 rounded-full shadow-lg flex items-center gap-1.5 ring-2 ring-white">
              <span class="material-symbols-outlined text-[13px] text-emerald-400 fill-1">check_circle</span>
              <span class="text-[10px] font-bold text-white">${shortName}</span>
              <span class="text-[10px] text-amber-300 font-semibold">★ ${score}</span>
            </div>
            <div class="w-2 h-2 bg-primary-container rotate-45 mx-auto -mt-1 ring-1 ring-white"></div>
          </div>
        `;
      } else if (isApril) {
        return `
          <div class="absolute cursor-pointer -translate-x-1/2 -translate-y-1/2 group z-20 transition-transform hover:scale-110 active:scale-95"
               style="left: ${c.coords.x}%; top: ${c.coords.y}%;"
               onclick="focusCafeCard('${c.id}')"
               title="${c.name}">
            <div class="bg-surface-container-lowest text-primary px-2.5 py-1 rounded-full shadow-md flex items-center gap-1.5 border border-outline-variant/30">
              <span class="material-symbols-outlined text-[13px] text-secondary fill-1">verified</span>
              <span class="text-[10px] font-bold text-primary">${shortName}</span>
              <span class="text-[10px] text-secondary font-semibold">★ ${score}</span>
            </div>
            <div class="w-2 h-2 bg-surface-container-lowest rotate-45 mx-auto -mt-1 border-r border-b border-outline-variant/30"></div>
          </div>
        `;
      } else {
        return `
          <div class="absolute cursor-pointer -translate-x-1/2 -translate-y-1/2 group z-20 transition-transform hover:scale-110 active:scale-95"
               style="left: ${c.coords.x}%; top: ${c.coords.y}%;"
               onclick="focusCafeCard('${c.id}')"
               title="${c.name}">
            <div class="bg-surface-container-lowest text-on-surface px-2.5 py-1 rounded-full shadow-md flex items-center gap-1.5 border border-outline-variant/30">
              <span class="material-symbols-outlined text-[13px] text-outline">bookmark</span>
              <span class="text-[10px] font-semibold text-on-surface">${shortName}</span>
              <span class="text-[10px] text-on-surface-variant font-medium">${score}</span>
            </div>
            <div class="w-2 h-2 bg-surface-container-lowest rotate-45 mx-auto -mt-1 border-r border-b border-outline-variant/30"></div>
          </div>
        `;
      }
    }).join('');
  }

  // Render Cafe Dossier Cards
  if (list.length === 0) {
    container.innerHTML = `
      <div class="p-8 text-center text-on-surface-variant flex flex-col items-center">
        <span class="material-symbols-outlined text-4xl mb-2 text-outline">search_off</span>
        <p class="font-serif text-base font-semibold text-primary">No cafes found</p>
        <p class="text-xs mt-1">Try resetting filters or expanding your search radius.</p>
      </div>
    `;
    return;
  }

  container.innerHTML = list.map((c) => {
    const isVisited = c.visitedStatus === 'VISITED';
    const isApril = c.id === 'cafe-2';
    const highlight = c.signatureBar || (c.communityRenowned ? 'Direct Trade & Geisha Specialty' : 'Single Origin Pour Over Bar');

    return `
      <div id="cafe-card-${c.id}" class="card-minimal p-3.5 flex flex-col gap-3 transition-all hover:border-secondary/30">
        <!-- Media & Main Header -->
        <div class="flex gap-3">
          <div class="w-20 h-20 rounded-xl overflow-hidden shrink-0 bg-surface-container relative">
            <img class="w-full h-full object-cover" src="${c.photoUrl}" alt="${c.name}">
            <span class="absolute top-1 left-1 px-1.5 py-0.5 rounded text-[9px] font-bold ${isVisited ? 'bg-primary text-on-primary' : 'bg-surface/90 text-on-surface'}">
              ${isVisited ? 'Visited' : 'Wishlist'}
            </span>
          </div>
          <div class="flex-1 min-w-0 flex flex-col justify-between">
            <div>
              <div class="flex items-center justify-between gap-1">
                <span class="text-[11px] font-semibold text-secondary uppercase tracking-wider truncate">${c.neighborhood}</span>
                <div class="flex items-center gap-0.5 text-secondary shrink-0">
                  <span class="material-symbols-outlined text-[13px] fill-1">star</span>
                  <span class="text-xs font-bold text-primary">${c.communityRating || c.userRating}</span>
                </div>
              </div>
              <h3 class="font-serif text-base font-bold text-primary tracking-tight truncate mt-0.5">${c.name}</h3>
              <p class="text-xs text-on-surface-variant truncate mt-0.5">${c.distance} • ${c.hours}</p>
            </div>
            <p class="text-[11px] text-on-surface-variant/80 truncate mt-1">${highlight}</p>
          </div>
        </div>

        <!-- Action Row -->
        <div class="flex items-center justify-between pt-2 border-t border-card-border gap-2">
          <button onclick="prefillLoggerWithCafe('${c.id}')" class="flex-1 py-1.5 px-3 rounded-lg bg-primary text-on-primary text-xs font-bold hover:bg-secondary transition-colors text-center">
            ${isVisited ? 'Log Visit' : 'Add to Visited'}
          </button>
          ${c.liveMenu ? `
            <button onclick="openLiveMenu('${c.id}')" class="py-1.5 px-3 rounded-lg bg-surface-container text-xs font-semibold text-on-surface hover:bg-surface-container-high transition-colors">
              Menu
            </button>
          ` : ''}
          <button onclick="openDirections('${c.id}')" aria-label="Directions" class="w-8 h-8 rounded-lg bg-surface-container flex items-center justify-center text-on-surface-variant hover:text-primary transition-colors" title="Directions">
            <span class="material-symbols-outlined text-[16px]">directions</span>
          </button>
          <button onclick="${isVisited ? `shareCafePassport('${c.id}')` : `toggleWishlist('${c.id}')`}" aria-label="Bookmark" class="w-8 h-8 rounded-lg bg-surface-container flex items-center justify-center text-on-surface-variant hover:text-primary transition-colors" title="${isVisited ? 'Share' : 'Bookmark'}">
            <span class="material-symbols-outlined text-[16px] ${!isVisited && c.visitedStatus === 'WISHLIST' ? 'text-secondary fill-1' : ''}">${isVisited ? 'share' : (c.visitedStatus === 'WISHLIST' ? 'favorite' : 'bookmark')}</span>
          </button>
        </div>
      </div>
    `;
  }).join('');
}

// Render Sensory Analytics Screen & 5-Axis Radar Chart
function renderAnalyticsScreen() {
  const metrics = calculatePalateMetrics();

  // 1. Update Enthusiast Passport Hero Stats
  const totalBrewsEl = document.getElementById('analyticsTotalBrews');
  const originsEl = document.getElementById('analyticsOriginsExplored');
  const topProfileEl = document.getElementById('analyticsTopProfile');
  if (totalBrewsEl) totalBrewsEl.textContent = `${Math.max(48, (store.brews || []).length)}`;
  if (originsEl) originsEl.textContent = '14';
  if (topProfileEl) topProfileEl.textContent = 'Washed ETH';

  // 2. Update Quick Parameter Details
  const clarityBadge = document.getElementById('metricClarityBadge');
  if (clarityBadge) clarityBadge.textContent = `${metrics.clarity}%`;

  // 3. Draw 5-Axis SVG Radar Polygon (Center: 140, 130, Radius: 100)
  const cx = 140;
  const cy = 130;
  const maxR = 100;

  // Exact 5-axis definitions matching Stitch prototype:
  // Acidity: -90° (top)
  // Sweetness: -18°
  // Body: 54°
  // Clarity: 126°
  // Bitterness: 198°
  const axes = [
    { key: 'acidity', val: metrics.acidity, angle: -90, labelId: 'radarLabelAcidity', prefix: 'ACIDITY' },
    { key: 'sweetness', val: metrics.sweetness, angle: -18, labelId: 'radarLabelSweetness', prefix: 'SWEET' },
    { key: 'body', val: metrics.body, angle: 54, labelId: 'radarLabelBody', prefix: 'BODY' },
    { key: 'clarity', val: metrics.clarity, angle: 126, labelId: 'radarLabelClarity', prefix: 'CLARITY' },
    { key: 'bitterness', val: metrics.bitterness, angle: 198, labelId: 'radarLabelBitterness', prefix: 'BITTER' }
  ];

  const points = axes.map(a => {
    const rad = (a.angle * Math.PI) / 180;
    const r = (a.val / 100) * maxR;
    const x = cx + r * Math.cos(rad);
    const y = cy + r * Math.sin(rad);
    return `${Math.round(x)},${Math.round(y)}`;
  }).join(' ');

  const polygonEl = document.getElementById('radarDataPolygon');
  if (polygonEl) {
    polygonEl.setAttribute('points', points);
  }

  // Update nodes and labels
  axes.forEach((a, idx) => {
    const pt = document.getElementById(`radarPoint${idx}`);
    if (pt) {
      const rad = (a.angle * Math.PI) / 180;
      const r = (a.val / 100) * maxR;
      pt.setAttribute('cx', Math.round(cx + r * Math.cos(rad)));
      pt.setAttribute('cy', Math.round(cy + r * Math.sin(rad)));
    }
    const labelEl = document.getElementById(a.labelId);
    if (labelEl) {
      labelEl.textContent = `${a.prefix} (${a.val}%)`;
    }
  });
}

// Page 5 Specific Interactive Action Handlers
let methodSortAsc = false;
function toggleMethodSort() {
  methodSortAsc = !methodSortAsc;
  const listEl = document.getElementById('methodEfficacyList');
  if (!listEl) return;
  const items = Array.from(listEl.children);
  items.reverse().forEach(item => listEl.appendChild(item));
  showToast(methodSortAsc ? "Sorted methods by rating (lowest first)" : "Sorted methods by rating (highest first)");
}

function filterAromaLogs(aroma) {
  showToast(`Filtering cupping archive for "${aroma}"`);
}

function exploreRecommendedBean(name, origin, process) {
  openModal('modal-log-brew');
  const notesInput = document.getElementById('logTastingNotes');
  if (notesInput) {
    notesInput.value = `Exploring ${name} (${origin} • ${process})`;
  }
  showToast(`Loaded ${name} into brew dial-in`);
}

function shareRadarCard() {
  const metrics = calculatePalateMetrics();
  const summary = `AfterTaste Palate Passport — Seasoned Cupper | Clarity: ${metrics.clarity}%, Acidity: ${metrics.acidity}%, Sweet: ${metrics.sweetness}%, Body: ${metrics.body}%, Bitter: ${metrics.bitterness}%`;
  if (navigator.clipboard) {
    navigator.clipboard.writeText(summary).catch(() => {});
  }
  showToast("Sensory radar card copied to clipboard! 📋");
}

// --- 6. INTERACTION & ACTION HANDLERS ---

function selectTab(tabKey) {
  store.activeTab = tabKey;
  
  // Hide all screens
  ['journal', 'vault', 'cafes', 'analytics'].forEach(tab => {
    const el = document.getElementById(`screen-${tab}`);
    if (el) el.classList.toggle('hidden', tab !== tabKey);
  });

  // Highlight bottom nav buttons
  document.querySelectorAll('.bottom-nav-item').forEach(btn => {
    const isTarget = btn.getAttribute('data-tab') === tabKey;
    btn.classList.toggle('text-secondary', isTarget);
    btn.classList.toggle('font-bold', isTarget);
    btn.classList.toggle('text-on-surface-variant', !isTarget);
  });

  // Update subtitle in top app bar
  const subEl = document.getElementById('topBarSubtitle');
  if (subEl) {
    const dict = I18N[store.lang] || I18N.en;
    const titles = {
      journal: 'journal',
      vault: 'bean vault',
      cafes: 'cafe map',
      analytics: 'analytics'
    };
    subEl.textContent = `— ${titles[tabKey] || tabKey}`;
  }

  // Render content of active tab
  if (tabKey === 'journal') renderJournalScreen();
  else if (tabKey === 'vault') renderVaultScreen();
  else if (tabKey === 'cafes') {
    renderCafesScreen();
    setTimeout(initOsmMap, 150);
  }
  else if (tabKey === 'analytics') renderAnalyticsScreen();

  window.scrollTo({ top: 0, behavior: 'smooth' });
}

function openModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
  }
}

function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.classList.remove('active');
    document.body.style.overflow = '';
  }
}

function showToast(message) {
  const toast = document.getElementById('globalToastBar');
  const msgEl = document.getElementById('globalToastMessage');
  if (toast && msgEl) {
    msgEl.textContent = message;
    toast.classList.add('active');
    setTimeout(() => {
      toast.classList.remove('active');
    }, 2800);
  }
}

function quickDeductGrams(beanId, grams) {
  const bean = store.beans.find(b => b.id === beanId);
  if (bean) {
    bean.weightRemaining = Math.max(0, bean.weightRemaining - grams);
    if (bean.weightRemaining === 0) bean.status = 'FINISHED';
    store.save('aftertaste_beans', store.beans);
    renderVaultScreen();
    showToast(`Deducted ${grams}g from ${bean.name}. Remaining: ${bean.weightRemaining}g`);
  }
}

function toggleWishlist(cafeId) {
  store.toggleWishlistCafe(cafeId);
  renderCafesScreen();
  showToast("Updated cafe wishlist status");
}

function focusCafeCard(cafeId) {
  const card = document.getElementById(`cafe-card-${cafeId}`);
  if (card) {
    card.scrollIntoView({ behavior: 'smooth', block: 'center' });
    card.classList.add('ring-2', 'ring-secondary');
    setTimeout(() => {
      card.classList.remove('ring-2', 'ring-secondary');
    }, 2000);
  }
}



function toggleMapLayers() {
  showToast("Nordic Cartography: Switched to Satellite Hybrid Layer");
}

function scrollToCafesList() {
  const container = document.getElementById('cafesCardsContainer');
  if (container) {
    container.scrollIntoView({ behavior: 'smooth' });
  }
}

function openDirections(cafeId) {
  const cafe = store.cafes.find(c => c.id === cafeId);
  if (cafe && cafe.lat && cafe.lng) {
    showToast(`Opening OpenStreetMap for ${cafe.name}... 🗺️`);
    window.open(`https://www.openstreetmap.org/?mlat=${cafe.lat}&mlon=${cafe.lng}#map=17/${cafe.lat}/${cafe.lng}`, '_blank');
  } else {
    const name = cafe ? cafe.name : "Cafe";
    showToast(`Opening OpenStreetMap search for ${name}... 🗺️`);
    window.open(`https://www.openstreetmap.org/search?query=${encodeURIComponent(name)}`, '_blank');
  }
}

function shareCafePassport(cafeId) {
  const cafe = store.cafes.find(c => c.id === cafeId);
  if (!cafe) return;
  if (navigator.share) {
    navigator.share({
      title: `${cafe.name} • AfterTaste Passport`,
      text: `Check out my tasting dossier for ${cafe.name} on AfterTaste!`,
      url: window.location.href
    }).catch(() => {});
  } else {
    showToast(`Dossier for ${cafe.name} copied to clipboard! 📋`);
  }
}

function showCafeHours(cafeId) {
  const cafe = store.cafes.find(c => c.id === cafeId);
  if (cafe) {
    showToast(`${cafe.name}: ${cafe.hours}`);
  }
}

function openLiveMenu(cafeId) {
  const cafe = store.cafes.find(c => c.id === cafeId);
  if (!cafe) return;

  const titleEl = document.getElementById('liveMenuCafeTitle');
  const itemsContainer = document.getElementById('liveMenuItemsContainer');
  if (titleEl) titleEl.textContent = `${cafe.name} • Live Offerings`;

  if (itemsContainer) {
    itemsContainer.innerHTML = (cafe.liveMenu || []).map(item => `
      <div class="flex items-center justify-between p-3 rounded-xl bg-surface-container border border-outline-variant/30">
        <div>
          <span class="text-[11px] uppercase tracking-wider text-secondary font-bold">${item.category}</span>
          <h5 class="font-serif text-sm font-semibold text-primary">${item.item}</h5>
        </div>
        <span class="text-sm font-bold text-primary">${item.price}</span>
      </div>
    `).join('');
  }

  openModal('modal-live-menu');
}

let currentLogMethod = 'Single Origin Pour Over';
let currentLogTemp = 'Hot';
let currentLogVessel = 'For Here';
let currentLogRating = 4.5;

function selectMethod(btn, methodName, subtitle) {
  document.querySelectorAll('.method-card').forEach(card => {
    card.classList.remove('bg-primary', 'text-on-primary', 'shadow-md', 'active');
    card.classList.add('bg-surface-container', 'text-on-surface');
    const label = card.querySelector('.text-xs');
    if (label) {
      label.classList.remove('text-surface-bright');
      label.classList.add('text-primary');
    }
    const icon = card.querySelector('.material-symbols-outlined');
    if (icon) {
      icon.classList.add('text-on-surface-variant');
    }
  });
  btn.classList.add('bg-primary', 'text-on-primary', 'shadow-md', 'active');
  btn.classList.remove('bg-surface-container', 'text-on-surface');
  const activeLabel = btn.querySelector('.text-xs');
  if (activeLabel) {
    activeLabel.classList.add('text-surface-bright');
    activeLabel.classList.remove('text-primary');
  }
  const activeIcon = btn.querySelector('.material-symbols-outlined');
  if (activeIcon) {
    activeIcon.classList.remove('text-on-surface-variant');
  }
  currentLogMethod = methodName;
  const label = document.getElementById('selectedMethodLabel');
  if (label) label.textContent = `${methodName} (${subtitle || 'Manual'})`;
}

function selectTemp(temp) {
  currentLogTemp = temp;
  const btnHot = document.getElementById('btnTempHot');
  const btnIced = document.getElementById('btnTempIced');
  const desc = document.getElementById('tempDescription');
  if (temp === 'Hot') {
    if (btnHot) btnHot.className = 'flex-1 py-1 px-2 rounded-lg bg-primary text-on-primary text-xs text-center font-semibold transition-colors';
    if (btnIced) btnIced.className = 'flex-1 py-1 px-2 rounded-lg bg-surface-container-high text-on-surface text-xs text-center transition-colors';
    if (desc) desc.textContent = 'Warm Ceramic Cup';
  } else {
    if (btnIced) btnIced.className = 'flex-1 py-1 px-2 rounded-lg bg-primary text-on-primary text-xs text-center font-semibold transition-colors';
    if (btnHot) btnHot.className = 'flex-1 py-1 px-2 rounded-lg bg-surface-container-high text-on-surface text-xs text-center transition-colors';
    if (desc) desc.textContent = 'Chilled Glass with Large Cube';
  }
}

function selectVessel(vessel) {
  currentLogVessel = vessel;
  const btnHere = document.getElementById('btnVesselHere');
  const btnToGo = document.getElementById('btnVesselToGo');
  const desc = document.getElementById('vesselDescription');
  if (vessel === 'For Here') {
    if (btnHere) btnHere.className = 'flex-1 py-1 px-2 rounded-lg bg-primary text-on-primary text-xs text-center font-semibold transition-colors';
    if (btnToGo) btnToGo.className = 'flex-1 py-1 px-2 rounded-lg bg-surface-container-high text-on-surface text-xs text-center transition-colors';
    if (desc) desc.textContent = 'Handmade Pottery';
  } else {
    if (btnToGo) btnToGo.className = 'flex-1 py-1 px-2 rounded-lg bg-primary text-on-primary text-xs text-center font-semibold transition-colors';
    if (btnHere) btnHere.className = 'flex-1 py-1 px-2 rounded-lg bg-surface-container-high text-on-surface text-xs text-center transition-colors';
    if (desc) desc.textContent = 'Compostable Takeaway Cup';
  }
}

function toggleTag(btn) {
  const isSelected = btn.classList.contains('bg-primary');
  if (isSelected) {
    btn.classList.remove('bg-primary', 'text-on-primary');
    btn.classList.add('bg-surface-container', 'text-on-surface');
    const checkIcon = btn.querySelector('.material-symbols-outlined');
    if (checkIcon) checkIcon.remove();
  } else {
    btn.classList.add('bg-primary', 'text-on-primary');
    btn.classList.remove('bg-surface-container', 'text-on-surface');
    const iconSpan = document.createElement('span');
    iconSpan.className = 'material-symbols-outlined text-[14px]';
    iconSpan.textContent = 'check';
    btn.appendChild(iconSpan);
  }
  const count = document.querySelectorAll('#flavorTagsGroup .tag-chip.bg-primary').length;
  const countEl = document.getElementById('selectedTagsCount');
  if (countEl) countEl.textContent = `${count} Selected`;
}

function addCustomFlavorTag() {
  const custom = prompt("Enter a sensory cupping note or flavor descriptor:", "Bergamot & Earl Grey");
  if (custom && custom.trim()) {
    const group = document.getElementById('flavorTagsGroup');
    const newBtn = document.createElement('button');
    newBtn.type = 'button';
    newBtn.className = 'tag-chip px-3 py-1.5 rounded-full bg-primary text-on-primary text-xs font-semibold flex items-center gap-1 transition-all';
    newBtn.onclick = function() { toggleTag(this); };
    newBtn.innerHTML = `<span>${custom.trim()}</span><span class="material-symbols-outlined text-[14px]">check</span>`;
    group.insertBefore(newBtn, group.lastElementChild);
    const count = document.querySelectorAll('#flavorTagsGroup .tag-chip.bg-primary').length;
    const countEl = document.getElementById('selectedTagsCount');
    if (countEl) countEl.textContent = `${count} Selected`;
    showToast(`Added custom descriptor: ${custom.trim()}`);
  }
}

function setRating(score) {
  currentLogRating = score;
  const starContainer = document.getElementById('starContainer');
  if (starContainer) {
    const stars = starContainer.querySelectorAll('.material-symbols-outlined');
    stars.forEach((star, index) => {
      if (index < score) {
        star.textContent = 'star';
        star.style.fontVariationSettings = "'FILL' 1";
      } else {
        star.textContent = 'star';
        star.style.fontVariationSettings = "'FILL' 0";
      }
    });
  }
  const label = document.getElementById('scoreLabel');
  if (label) {
    label.textContent = `${score}.0 of 5.0 (Exceptional)`;
  }
}

let sampleCafeIdx = 0;
function cycleSampleCafes() {
  sampleCafeIdx = (sampleCafeIdx + 1) % store.cafes.length;
  const cafe = store.cafes[sampleCafeIdx];
  const nameEl = document.getElementById('logSelectedCafeName');
  if (nameEl) nameEl.textContent = cafe.name;
  showToast(`Switched active cafe stop to: ${cafe.name}`);
}

let sampleBeanIdx = 0;
function cycleSampleBeans() {
  sampleBeanIdx = (sampleBeanIdx + 1) % store.beans.length;
  const bean = store.beans[sampleBeanIdx];
  const titleEl = document.getElementById('logBeanTitle');
  const roasterEl = document.getElementById('logBeanRoaster');
  const lotBadge = document.getElementById('logBeanLotBadge');
  const imgEl = document.getElementById('logBeanImg');
  if (titleEl) titleEl.textContent = bean.name;
  if (roasterEl) roasterEl.textContent = `${bean.roaster} • ${bean.process}`;
  if (lotBadge) lotBadge.textContent = `${bean.origin} • Lot #0${sampleBeanIdx + 42}`;
  if (imgEl && bean.photoUrl) imgEl.src = bean.photoUrl;
  showToast(`Switched single origin bean: ${bean.name}`);
}

function promptPhotoUpload() {
  showToast("Photo attached: IMG_Prolog_CeramicCup.heic 📸");
}

function shareLogPreview() {
  showToast("Brew log recipe snapshot copied to clipboard! 📋");
}

function prefillLoggerWithBean(beanId) {
  const bean = store.beans.find(b => b.id === beanId);
  if (!bean) return;
  const titleEl = document.getElementById('logBeanTitle');
  const roasterEl = document.getElementById('logBeanRoaster');
  const lotBadge = document.getElementById('logBeanLotBadge');
  const imgEl = document.getElementById('logBeanImg');
  if (titleEl) titleEl.textContent = bean.name;
  if (roasterEl) roasterEl.textContent = `${bean.roaster} • ${bean.process}`;
  if (lotBadge) lotBadge.textContent = `${bean.origin} • ${bean.elevation || '1,800m'}`;
  if (imgEl && bean.photoUrl) imgEl.src = bean.photoUrl;
  openModal('modal-log-brew');
}

function prefillLoggerWithCafe(cafeId) {
  const cafe = store.cafes.find(c => c.id === cafeId);
  if (!cafe) return;
  const nameEl = document.getElementById('logSelectedCafeName');
  if (nameEl) nameEl.textContent = cafe.name;
  const cafeSelect = document.getElementById('logCafeSelect');
  if (cafeSelect) cafeSelect.value = cafe.name;
  openModal('modal-log-brew');
}

function onCafeDropdownSelect(cafeName) {
  const nameEl = document.getElementById('logSelectedCafeName');
  if (nameEl) nameEl.textContent = cafeName;
  const cafe = store.cafes.find(c => c.name === cafeName);
  if (cafe && cafe.tastedItems && cafe.tastedItems.length > 0) {
    const item = cafe.tastedItems[0];
    const priceInput = document.getElementById('logPriceInput');
    if (priceInput && item.price) {
      const numOnly = item.price.replace(/[^0-9]/g, '');
      if (numOnly) priceInput.value = numOnly;
    }
  }
  showToast(`Selected cafe: ${cafeName}`);
}

function onBeanDropdownSelect(beanName) {
  const titleEl = document.getElementById('logBeanTitle');
  const lotBadge = document.getElementById('logBeanLotBadge');
  if (titleEl) titleEl.textContent = beanName;
  if (lotBadge) lotBadge.textContent = "Microlot Selection";
  showToast(`Selected bean: ${beanName}`);
}

function updatePriceDisplay(val) {
  const display = document.getElementById('logPriceDisplay');
  if (display) display.textContent = `₹${val}`;
}

// OpenStreetMap & Leaflet.js (100% Free Open-Source Map)
let osmMapInstance = null;
let osmMarkers = [];

function initOsmMap() {
  const mapEl = document.getElementById('osmMap');
  if (!mapEl || !window.L) return;

  if (osmMapInstance) {
    osmMapInstance.invalidateSize();
    updateOsmMarkers();
    return;
  }

  // Initialize Leaflet Map (CartoDB Voyager / OpenStreetMap tiles)
  osmMapInstance = L.map('osmMap', {
    zoomControl: false,
    attributionControl: false
  }).setView([55.6761, 12.5683], 13);

  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap contributors'
  }).addTo(osmMapInstance);

  updateOsmMarkers();

  // Automatically attempt to center on user location
  if (navigator.geolocation) {
    recenterMap(false);
  }
}

function updateOsmMarkers() {
  if (!osmMapInstance || !window.L) return;

  // Clear existing markers
  osmMarkers.forEach(m => osmMapInstance.removeLayer(m));
  osmMarkers = [];

  const bounds = [];
  store.cafes.forEach(cafe => {
    if (!cafe.lat || !cafe.lng) return;
    const latLng = [cafe.lat, cafe.lng];
    bounds.push(latLng);

    const isVisited = cafe.visitedStatus === 'VISITED';
    const customIcon = L.divIcon({
      className: 'osm-cafe-marker',
      html: `
        <div style="background-color: ${isVisited ? '#2c1810' : '#c86d3b'}; color: #fff; padding: 4px 8px; border-radius: 9999px; font-size: 10px; font-weight: bold; display: flex; align-items: center; gap: 4px; box-shadow: 0 4px 10px rgba(0,0,0,0.3); border: 2px solid #fff; white-space: nowrap; cursor: pointer;">
          <span>☕</span>
          <span>${cafe.name.split(' ')[0]}</span>
          <span style="color: #ffdbc9;">★${cafe.communityRating || 4.8}</span>
        </div>
      `,
      iconSize: [80, 28],
      iconAnchor: [40, 14]
    });

    const marker = L.marker(latLng, { icon: customIcon }).addTo(osmMapInstance);
    marker.bindPopup(`
      <div style="font-family: inherit; font-size: 12px; color: #2c1810; padding: 2px;">
        <strong style="font-size: 13px;">${cafe.name}</strong><br>
        <span style="color: #74584E; font-size: 11px;">${cafe.neighborhood}</span><br>
        <span style="color: #c86d3b; font-weight: bold; font-size: 11px;">${cafe.distance} away</span>
        <div style="margin-top: 6px;">
          <button onclick="prefillLoggerWithCafe('${cafe.id}')" style="background: #2c1810; color: #fff; border: none; padding: 4px 10px; border-radius: 6px; font-size: 11px; font-weight: bold; cursor: pointer;">Log Cup Here</button>
        </div>
      </div>
    `);
    osmMarkers.push(marker);
  });

  if (bounds.length > 0) {
    osmMapInstance.fitBounds(bounds, { padding: [30, 30], maxZoom: 14 });
  }
}

let userLocationMarker = null;
let userLocationCoords = null;
let hasDiscoveredNearbyCafes = false;

function updateUserLocationPin(lat, lng) {
  if (!osmMapInstance || !window.L) return;
  userLocationCoords = { lat, lng };

  const userIcon = L.divIcon({
    className: 'osm-user-location-marker',
    html: `
      <div style="position: relative; width: 28px; height: 28px; display: flex; align-items: center; justify-content: center;">
        <div style="position: absolute; width: 28px; height: 28px; border-radius: 50%; background: rgba(59, 130, 246, 0.35); animation: ping 2s cubic-bezier(0, 0, 0.2, 1) infinite;"></div>
        <div style="width: 14px; height: 14px; border-radius: 50%; background: #2563eb; border: 2.5px solid #ffffff; box-shadow: 0 2px 8px rgba(0,0,0,0.4);"></div>
      </div>
    `,
    iconSize: [28, 28],
    iconAnchor: [14, 14]
  });

  if (userLocationMarker) {
    userLocationMarker.setLatLng([lat, lng]);
  } else {
    userLocationMarker = L.marker([lat, lng], { icon: userIcon, zIndexOffset: 2500 }).addTo(osmMapInstance);
    userLocationMarker.bindPopup(`
      <div style="font-family: inherit; font-size: 12px; color: #1e3a8a; padding: 2px;">
        <strong style="font-size: 13px;">📍 You are here</strong><br>
        <span style="color: #64748b; font-size: 11px;">Current GPS Location</span>
      </div>
    `);
  }
}

function recenterMap(explicit = true) {
  if (!osmMapInstance) return;

  if (userLocationCoords && !explicit) {
    osmMapInstance.flyTo([userLocationCoords.lat, userLocationCoords.lng], 15, { animate: true, duration: 1.0 });
    return;
  }

  if (navigator.geolocation) {
    if (explicit) showToast("Locating your position... 📍");

    navigator.geolocation.getCurrentPosition(
      (pos) => {
        const lat = pos.coords.latitude;
        const lng = pos.coords.longitude;
        updateUserLocationPin(lat, lng);

        // Center on user's real location with smooth flyTo animation
        osmMapInstance.flyTo([lat, lng], 15, { animate: true, duration: 1.2 });
        showToast("Centered to your location 📍");

        // Dynamically fetch and display real cafes around user's location
        fetchNearbyCafesFromOverpass(lat, lng);
      },
      (err) => {
        console.warn("Geolocation warning:", err.message);
        if (explicit) {
          if (err.code === 1) { // PERMISSION_DENIED
            showToast("Location permission denied. Please allow GPS in browser 📍");
          } else {
            showToast("Could not determine GPS position. Showing cafes 🗺️");
          }
        }
        fallbackFitAllCafes();
      },
      { enableHighAccuracy: true, timeout: 8000, maximumAge: 60000 }
    );
  } else {
    if (explicit) showToast("Geolocation not supported by browser. Showing cafes 🗺️");
    fallbackFitAllCafes();
  }
}

function fallbackFitAllCafes() {
  if (osmMapInstance && osmMarkers.length > 0) {
    const group = L.featureGroup(osmMarkers);
    osmMapInstance.fitBounds(group.getBounds().pad(0.15));
  }
}

function calculateDistanceInKm(lat1, lon1, lat2, lon2) {
  const R = 6371;
  const dLat = (lat2 - lat1) * Math.PI / 180;
  const dLon = (lon2 - lon1) * Math.PI / 180;
  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
  return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
}

async function fetchNearbyCafesFromOverpass(lat, lng) {
  if (hasDiscoveredNearbyCafes) return;

  // Check if user is already within 30km of existing loaded cafes
  const closestExisting = Math.min(...store.cafes.map(c => calculateDistanceInKm(lat, lng, c.lat, c.lng)));
  if (closestExisting < 30) {
    // User is already near predefined cafes: update their distances accurately
    store.cafes.forEach(c => {
      const d = calculateDistanceInKm(lat, lng, c.lat, c.lng);
      c.distance = `${d < 1 ? Math.round(d * 1000) + ' m' : d.toFixed(1) + ' km'}`;
    });
    renderCafeDirectory();
    updateOsmMarkers();
    return;
  }

  try {
    const query = `[out:json][timeout:12];(node["amenity"="cafe"](around:4000,${lat},${lng}););out center 15;`;
    const url = `https://overpass-api.de/api/interpreter?data=${encodeURIComponent(query)}`;
    const res = await fetch(url);
    if (!res.ok) return;
    const data = await res.json();

    if (data && data.elements && data.elements.length > 0) {
      hasDiscoveredNearbyCafes = true;
      const discovered = data.elements.map((el, i) => {
        const cLat = el.lat || (el.center && el.center.lat);
        const cLng = el.lon || (el.center && el.center.lon);
        const name = (el.tags && (el.tags.name || el.tags.brand)) || `Specialty Cafe ${i + 1}`;
        const street = (el.tags && (el.tags['addr:street'] || el.tags['addr:suburb'])) || 'Local Coffee Roaster';
        const d = calculateDistanceInKm(lat, lng, cLat, cLng);
        const distStr = d < 1 ? `${Math.round(d * 1000)} m` : `${d.toFixed(1)} km`;

        return {
          id: `osm-live-${el.id || i}`,
          name: name,
          neighborhood: street,
          distance: distStr,
          lat: cLat,
          lng: cLng,
          visitedStatus: 'NOT_VISITED',
          statusText: 'Nearby OpenStreetMap Spot',
          subBadge: 'Discovered',
          userRating: 4.8,
          communityRating: (4.4 + (i % 5) * 0.1).toFixed(1),
          reviewsCount: `${28 + i * 14} rev`,
          hours: (el.tags && el.tags.opening_hours) || 'Open Daily',
          photoUrl: 'https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&w=600&q=80',
          tastedItems: [
            { name: "Single Origin Pour Over", badge: "Specialty", notes: "Aromatic brew with clean floral notes", price: "₹290", rating: 4.8 },
            { name: "Cortado", badge: "Double Shot", notes: "Silky microfoam with rich espresso crema", price: "₹220", rating: 4.7 }
          ],
          liveMenu: [
            { category: "Espresso", item: "House Roast Espresso", price: "₹200", notes: "Balanced sweetness & rich body" },
            { category: "Filter", item: "Pour Over Single Origin", price: "₹290", notes: "Filter roast lot" },
            { category: "Cold", item: "Cold Brew On Tap", price: "₹240", notes: "Steeped 18 hours" }
          ]
        };
      });

      // Prepend discovered cafes to store
      store.cafes = [...discovered, ...store.cafes];
      updateOsmMarkers();
      renderCafeDirectory();
      populateLogCafeDropdown();
      showToast(`Found ${discovered.length} cafes near your location! ☕`);
    }
  } catch (err) {
    console.warn("Overpass fetch error:", err);
  }
}

// Google Sign-In State Handlers
function handleGoogleSignIn() {
  const googleUser = {
    isSignedIn: true,
    name: "Maya Lindqvist",
    email: "maya.coffee@gmail.com",
    avatar: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=200&q=80",
    lastSyncedAt: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  };
  localStorage.setItem('aftertaste_google_user', JSON.stringify(googleUser));
  store.googleUser = googleUser;
  updateGoogleAccountUI();
  showToast("Signed in with Google! Cloud Sync Active ☁️");
}

function handleGoogleSignOut() {
  const googleUser = { isSignedIn: false };
  localStorage.setItem('aftertaste_google_user', JSON.stringify(googleUser));
  store.googleUser = googleUser;
  updateGoogleAccountUI();
  showToast("Signed out of Google Account");
}

function updateGoogleAccountUI() {
  const signedInCard = document.getElementById('googleSignedInCard');
  const signedOutCard = document.getElementById('googleSignedOutCard');
  const nameEl = document.getElementById('userGoogleName');
  const emailEl = document.getElementById('userGoogleEmail');
  const syncStatus = document.getElementById('googleSyncStatus');

  const savedUser = JSON.parse(localStorage.getItem('aftertaste_google_user') || '{"isSignedIn":true,"name":"Maya Lindqvist","email":"maya.coffee@gmail.com"}');

  if (savedUser && savedUser.isSignedIn !== false) {
    if (signedInCard) signedInCard.classList.remove('hidden');
    if (signedOutCard) signedOutCard.classList.add('hidden');
    if (nameEl) nameEl.textContent = savedUser.name || "Maya Lindqvist";
    if (emailEl) emailEl.textContent = savedUser.email || "maya.coffee@gmail.com";
    if (syncStatus) syncStatus.textContent = `Cloud Sync: Synced at ${savedUser.lastSyncedAt || 'Today 00:30'}`;
  } else {
    if (signedInCard) signedInCard.classList.add('hidden');
    if (signedOutCard) signedOutCard.classList.remove('hidden');
  }
}

function saveBrewLog() {
  const saveBtn = document.getElementById('saveLogBtn');
  if (!saveBtn) return;
  const originalHtml = saveBtn.innerHTML;
  saveBtn.disabled = true;
  saveBtn.innerHTML = `
    <span class="material-symbols-outlined text-[18px] animate-spin">progress_activity</span>
    <span>Logging Ritual...</span>
  `;

  setTimeout(() => {
    saveBtn.innerHTML = `
      <span class="material-symbols-outlined text-[18px] text-emerald-400">check_circle</span>
      <span>Saved to Field Ledger!</span>
    `;

    const cafeName = document.getElementById('logCafeSelect')?.value || document.getElementById('logSelectedCafeName')?.textContent || 'Prolog Coffee Bar';
    const beanName = document.getElementById('logBeanSelect')?.value || document.getElementById('logBeanTitle')?.textContent || 'Ethiopia Gesha Village';
    const roaster = document.getElementById('logBeanRoaster')?.textContent?.split('•')[0]?.trim() || 'Onyx Coffee Lab';
    const notesText = document.getElementById('cuppingNotes')?.value || 'Super clean cup, bright citric acidity, delicate floral finish.';
    const selectedTags = Array.from(document.querySelectorAll('#flavorTagsGroup .tag-chip.bg-primary span:first-child')).map(s => s.textContent.trim());
    const priceVal = document.getElementById('logPriceInput')?.value || '350';
    const milkVal = document.getElementById('logMilkSelect')?.value || 'Oat';

    const newBrew = {
      id: `brew-${Date.now()}`,
      cafeName,
      roaster,
      beanName,
      method: currentLogMethod || 'Pour Over',
      methodBadge: currentLogMethod || 'Pour Over',
      ratio: '15g / 240g • Filter',
      price: `₹${priceVal}`,
      temperature: currentLogTemp,
      vessel: currentLogVessel,
      milkOption: milkVal,
      tastingNotes: selectedTags.length > 0 ? selectedTags : ['Floral & Jasmine', 'Stone Fruit', 'Berry & Blackberry'],
      rating: currentLogRating || 4.5,
      notes: notesText,
      photoUrl: 'https://lh3.googleusercontent.com/aida-public/AB6AXuDoL9ULxF80sNM_IzngYfSeWTPAd7EM7Jcc5q-YmsdmhCvt_kOo7HubGXWvQ8xGaZzo4llqokts7YbRBY0rAZtAy0GHPFQv82gnm6FbNqrTMzGDA_X6kWy6FbHoNUI0r6-AVLEh9P_ygTEaGHimC9Z5MbUZkUorjZSRy945p6R856rqePqh2IvXQq28Oy-0jptyCP1-3tDU8zn6455mNOznEm5Ko9bWUGPUAP0nVxsu20Qx3oTkD_pG',
      timeDisplay: 'Just Now',
      timestamp: new Date().toISOString()
    };

    store.addBrew(newBrew);

    setTimeout(() => {
      saveBtn.innerHTML = originalHtml;
      saveBtn.disabled = false;
      closeModal('modal-log-brew');
      renderJournalScreen();
      renderAnalyticsScreen();
      showToast(`Saved ${beanName} (₹${priceVal}) to Cafe Passport! ☕`);
    }, 1200);
  }, 700);
}

// --- 7. INITIALIZATION ---
document.addEventListener('DOMContentLoaded', () => {
  // Apply saved theme
  store.setTheme(store.theme);

  // Theme toggle button
  const themeToggle = document.getElementById('themeToggleBtn');
  if (themeToggle) {
    themeToggle.addEventListener('click', () => {
      const next = store.theme === 'dark' ? 'light' : 'dark';
      store.setTheme(next);
      themeToggle.querySelector('.material-symbols-outlined').textContent = next === 'dark' ? 'light_mode' : 'dark_mode';
    });
  }

  // Language switcher
  const langSelect = document.getElementById('langSelectBtn');
  if (langSelect) {
    langSelect.addEventListener('click', () => {
      const order = ['en', 'da', 'ja', 'fr'];
      const nextIdx = (order.indexOf(store.lang) + 1) % order.length;
      store.setLang(order[nextIdx]);
      langSelect.querySelector('.lang-code').textContent = order[nextIdx].toUpperCase();
      updateGreetingHeader();
      showToast(`Language set to ${order[nextIdx].toUpperCase()}`);
    });
  }

  // Bottom Navigation tabs
  document.querySelectorAll('.bottom-nav-item').forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      const tab = btn.getAttribute('data-tab');
      selectTab(tab);
    });
  });

  // FAB Quick Log
  const fabBtn = document.getElementById('fabQuickLogBtn');
  if (fabBtn) {
    fabBtn.addEventListener('click', () => {
      openModal('modal-log-brew');
    });
  }

  // Journal Filter Buttons
  document.querySelectorAll('.journal-filter-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.journal-filter-btn').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      store.journalFilter = btn.getAttribute('data-filter');
      renderJournalScreen();
    });
  });

  // Vault Segment Buttons
  document.querySelectorAll('.vault-segment-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.vault-segment-btn').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      store.vaultSegment = btn.getAttribute('data-segment');
      renderVaultScreen();
    });
  });

  // Vault Search
  const vaultSearch = document.getElementById('vaultSearchInput');
  if (vaultSearch) {
    vaultSearch.addEventListener('input', () => {
      renderVaultScreen();
    });
  }

  // Cafe Filter Buttons
  document.querySelectorAll('.cafe-filter-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.cafe-filter-btn').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      store.cafeFilter = btn.getAttribute('data-filter');
      renderCafesScreen();
    });
  });

  // Cafe Search
  const cafeSearch = document.getElementById('cafeSearchInput');
  if (cafeSearch) {
    cafeSearch.addEventListener('input', () => {
      renderCafesScreen();
    });
  }

  // Log Brew Tag Pill Toggles
  document.querySelectorAll('.log-tag-pill').forEach(pill => {
    pill.addEventListener('click', () => {
      pill.classList.toggle('active');
    });
  });

  // Form Submit
  const logForm = document.getElementById('logBrewForm');
  if (logForm) {
    logForm.addEventListener('submit', handleLogBrewSubmit);
  }

  // Initialize view
  updateGreetingHeader();
  updateGoogleAccountUI();
  selectTab('journal');
});
