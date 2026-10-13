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
    price: "52 DKK",
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
    price: "46 DKK",
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
    price: "55 DKK",
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
    price: "48 DKK",
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
    price: "45 DKK",
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
    coords: { x: 24, y: 28 }, // Percentage on map canvas
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
        price: "52 DKK", 
        rating: 5.0 
      },
      { 
        name: "Cardamom Bun (Pastry)", 
        badge: "Fresh Bake",
        notes: "Buttery, crushed pod aroma, caramelized bottom", 
        price: "38 DKK", 
        rating: 4.8 
      }
    ],
    liveMenu: [
      { category: "Filter", item: "Ethiopia Shantawene", price: "48 DKK", notes: "Natural • Peach & Jasmine floral notes" },
      { category: "Filter", item: "Batch Brew Nordic", price: "36 DKK", notes: "Colombian Pink Bourbon rotative" },
      { category: "Espresso Bar", item: "Double Espresso (House Roast)", price: "32 DKK", notes: "Sweet clementine and dark chocolate" },
      { category: "Bakes", item: "Cardamom Twist", price: "38 DKK", notes: "Cardamom, sourdough, brown butter" }
    ]
  },
  {
    id: "cafe-2",
    name: "April Coffee Store",
    neighborhood: "Ryesgade 86, Østerbro",
    distance: "1.8 km",
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
        price: "75 DKK", 
        rating: 5.0 
      },
      { 
        name: "Canelé de Bordeaux", 
        badge: "Classic Bake",
        notes: "Crisp beeswax crust, rum & vanilla bean custard", 
        price: "35 DKK", 
        rating: 4.9 
      }
    ],
    signatureBar: "Aeropress Inverted & April Brewer Single Serves",
    liveMenu: [
      { category: "April Brewer Lot", item: "Kenya AA Single Origin", price: "58 DKK", notes: "Blackcurrant & cane sugar" },
      { category: "April Brewer Lot", item: "Panama Geisha Natural", price: "95 DKK", notes: "Jasmine floral & mango" },
      { category: "Espresso", item: "Flat White (Oat)", price: "46 DKK", notes: "Volcán Azul espresso blend" }
    ]
  },
  {
    id: "cafe-3",
    name: "Coffee Collective Bernikow",
    neighborhood: "Kristen Bernikows Gade 2",
    distance: "2.1 km",
    coords: { x: 60, y: 72 },
    visitedStatus: "WISHLIST",
    statusText: "Wishlist • Not Yet Visited",
    subBadge: "Iconic Spot",
    userRating: null,
    communityRating: 4.7,
    reviewsCount: "1.2k rev",
    hours: "Open until 19:00",
    photoUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuCSyL119qKW3JyThDsVpSotzLLFqwhmDRtyydZHMkIsAYUxgUNc-DtyeFelqSCyF6iT-PrDukq07M7ImqvF7nKMum2JNn2NxL0V2h4qSsiJunCxyHwhUk9e1jx-dx0KERoP8Se3kooO0p0J0dormZeE2m1bSd1L1X48CRsiIdH_E6kBwQ61TnWftGBcdHzl6z8Fh-XKvi1u8-EQEXO8X_v3Ng2vQaytWo2OlLcfEtjNJ-hz-1KWywpl",
    communityRenowned: "Direct Trade Pioneer • Celebrated for single estate Bolivian Takesi Geisha and signature house-made coffee soft serve infused with Kieni espresso.",
    fetchedMenu: [
      { item: "Kieni Espresso", price: "36 DKK", tag: "Blackcurrant" },
      { item: "Karatina Chemex", price: "54 DKK", tag: "Crisp Citrus" },
      { item: "Cascara Kombucha", price: "42 DKK", tag: "Sparkling Tea" }
    ],
    tastedItems: [],
    liveMenu: [
      { category: "Pour Over", item: "Kieni, Kenya (Washed)", price: "50 DKK", notes: "Intense blackcurrant & rhubarb" },
      { category: "Espresso", item: "Takesi Geisha, Bolivia", price: "45 DKK", notes: "Bergamot & white peach" },
      { category: "Soft Serve", item: "Coffee Soft Ice with Crisp", price: "45 DKK", notes: "Infused with cold drip concentrate" }
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
      <div class="bg-surface-container rounded-xl p-3.5 shadow-sm flex flex-col space-y-2.5 active:scale-[0.99] transition-transform border border-outline-variant/30">
        <!-- Top Ribbon: Method Badge & Time -->
        <div class="flex items-start justify-between">
          <div class="flex items-center gap-2">
            <span class="px-2 py-0.5 rounded-full ${badgeClass} font-label-sm text-[11px] font-semibold">
              ${badgeText}
            </span>
            <span class="text-[11px] text-on-surface-variant">${timeDisplay}</span>
          </div>
          <div class="flex items-center gap-0.5 text-secondary">
            <span class="material-symbols-outlined text-[14px]" style="font-variation-settings: 'FILL' 1;">star</span>
            <span class="text-xs font-bold text-primary">${b.rating.toFixed(1)}</span>
          </div>
        </div>

        <!-- Coffee Details & Thumbnail -->
        <div class="flex items-center justify-between gap-3">
          <div class="min-w-0 flex-1">
            <h4 class="font-serif text-sm font-bold text-primary truncate">${b.beanName}</h4>
            <p class="text-xs text-on-surface-variant truncate">${b.roaster} • ${b.ratio}</p>
          </div>
          <div class="w-12 h-12 rounded-lg overflow-hidden flex-shrink-0 bg-surface-container-high shadow-sm">
            <img class="w-full h-full object-cover" src="${b.photoUrl}" alt="${b.beanName}">
          </div>
        </div>

        <!-- Cupping Notes -->
        <div class="flex flex-wrap gap-1.5">
          ${(b.tastingNotes || []).map(note => `
            <span class="px-2 py-0.5 rounded-full bg-surface-container-low text-[10px] text-on-surface-variant font-medium">
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
      <article class="bg-surface-container-lowest rounded-2xl p-4 shadow-sm border border-outline-variant/30 transition-all hover:shadow-md flex flex-col gap-3">
        <!-- Card Header: Image & Main Origin Specs -->
        <div class="flex gap-3">
          <div class="relative w-20 h-24 rounded-xl overflow-hidden flex-shrink-0 bg-surface-container-high shadow-inner">
            <img src="${b.photoUrl || 'https://images.unsplash.com/photo-1587734195503-904fca47e0e9?auto=format&fit=crop&w=400&q=80'}" 
                 alt="${b.name}" class="w-full h-full object-cover">
            <div class="absolute top-1 left-1 bg-surface-container-lowest/90 backdrop-blur-sm rounded px-1.5 py-0.5">
              <span class="text-[10px] font-bold text-on-surface">${countryCode}</span>
            </div>
          </div>
          <div class="flex flex-col justify-between flex-1 min-w-0">
            <div>
              <div class="flex items-center justify-between gap-1">
                <span class="text-xs font-semibold uppercase tracking-wider text-secondary truncate">${b.roaster}</span>
                <span class="text-[11px] text-on-surface-variant flex-shrink-0">${b.roasterCity || b.origin}</span>
              </div>
              <h3 class="font-serif text-lg font-bold text-primary tracking-tight truncate mt-0.5">${b.name}</h3>
              <div class="flex items-center gap-1.5 mt-1">
                <span class="px-2 py-0.5 rounded-full bg-surface-container text-primary font-semibold text-[10px]">${b.process}</span>
                <span class="text-xs text-on-surface-variant truncate">${b.variety || 'Heirloom Variety'}</span>
              </div>
            </div>
            <!-- Rest Status Badge -->
            <div class="flex items-center gap-1.5 mt-2 bg-surface-container-low px-2 py-1 rounded-lg border border-outline-variant/20">
              <span class="w-2 h-2 rounded-full ${degassing.isPeak ? 'bg-emerald-500' : 'bg-secondary'}"></span>
              <span class="text-[11px] font-semibold text-primary">${degassing.badgeText}</span>
            </div>
          </div>
        </div>

        <!-- Flavor Notes Chips -->
        <div class="flex flex-wrap items-center gap-1.5 pt-0.5">
          ${(b.tastingNotes || []).map(note => `
            <span class="px-2.5 py-0.5 rounded-full bg-surface-container-low text-on-surface text-[11px] font-medium border border-outline-variant/20">
              ${note}
            </span>
          `).join('')}
        </div>

        <!-- Vault Inventory Capacity Bar -->
        <div class="bg-surface-container-low p-2.5 rounded-xl flex flex-col gap-1.5 border border-outline-variant/30">
          <div class="flex items-center justify-between text-xs">
            <span class="text-on-surface-variant flex items-center gap-1">
              <span class="material-symbols-outlined text-[15px]">scale</span>
              ${isLowInventory ? 'Low Inventory Warning' : isUnopened ? 'Unopened Bag' : 'Remaining Supply'}
            </span>
            <span class="font-bold ${isLowInventory ? 'text-secondary' : 'text-primary'}">
              ~${b.weightRemaining}g <span class="font-normal text-on-surface-variant">/ ${b.weightTotal}g (${percentRemaining}%)</span>
            </span>
          </div>
          <div class="w-full h-2 rounded-full bg-surface-container overflow-hidden">
            <div class="h-full rounded-full transition-all duration-500 ${isLowInventory ? 'bg-secondary' : 'bg-secondary-container'}" 
                 style="width: ${percentRemaining}%;"></div>
          </div>
        </div>

        <!-- Quick Log & Dial in shortcut -->
        <div class="flex items-center justify-between pt-1">
          <div class="flex items-center gap-1 text-on-surface-variant">
            <span class="material-symbols-outlined text-[16px] text-amber-500 fill-1">star</span>
            <span class="text-xs font-bold text-primary">${b.rating || '4.9'}</span>
            <span class="text-[11px] text-on-surface-variant">(${b.brewCount || 6} brews)</span>
          </div>
          <div class="flex items-center gap-2">
            <button onclick="quickDeductGrams('${b.id}', 15)" class="h-8 px-2.5 rounded-lg bg-surface-container hover:bg-surface-container-high text-on-surface text-xs font-semibold flex items-center gap-1 transition-all" title="Deduct 15g single brew">
              <span class="material-symbols-outlined text-[14px]">remove</span>
              <span>15g</span>
            </button>
            <button onclick="prefillLoggerWithBean('${b.id}')" class="h-8 px-3.5 rounded-lg bg-primary-container text-white hover:bg-secondary text-xs font-bold flex items-center gap-1.5 shadow-sm active:scale-95 transition-all">
              <span class="material-symbols-outlined text-[15px]">coffee</span>
              <span>${isUnopened ? 'First Brew' : 'Dial In'}</span>
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
    const isProlog = c.id === 'cafe-1';
    const isCollective = c.id === 'cafe-3';

    return `
      <div id="cafe-card-${c.id}" class="bg-surface-container-low rounded-2xl overflow-hidden shadow-sm flex flex-col border border-outline-variant/30 transition-all">
        <!-- Media Header & Badges -->
        <div class="relative h-44 w-full">
          <img class="w-full h-full object-cover" src="${c.photoUrl}" alt="${c.name}">
          <div class="absolute inset-0 bg-gradient-to-t from-primary-container/85 via-primary-container/20 to-transparent"></div>
          
          <div class="absolute top-2.5 left-2.5 flex items-center gap-1.5 flex-wrap">
            <span class="bg-primary-container/90 backdrop-blur text-surface px-2.5 py-1 rounded-full text-[10px] flex items-center gap-1 shadow-sm font-semibold">
              <span class="material-symbols-outlined text-[13px] text-emerald-400 fill-1">${isVisited ? (isApril ? 'workspace_premium' : 'check_circle') : 'bookmark_border'}</span>
              <span>${c.statusText || (isVisited ? 'Dined In • Visited' : 'Wishlist • Not Yet Visited')}</span>
            </span>
            ${c.subBadge ? `
              <span class="bg-surface-container-lowest/90 backdrop-blur text-secondary px-2.5 py-1 rounded-full text-[10px] font-semibold shadow-sm">
                ${c.subBadge}
              </span>
            ` : ''}
          </div>

          <div class="absolute bottom-2.5 left-3.5 right-3.5 flex items-end justify-between text-white">
            <div class="pr-2">
              <h2 class="font-serif text-lg font-semibold tracking-tight text-white drop-shadow">${c.name}</h2>
              <p class="text-[11px] text-surface-container-high opacity-95">${c.neighborhood} • ${c.distance} • ${c.hours}</p>
            </div>
            <div class="bg-surface-container-lowest text-primary px-2.5 py-1 rounded-xl shadow-md flex flex-col items-center leading-none flex-shrink-0">
              <div class="flex items-center gap-0.5">
                <span class="material-symbols-outlined text-[14px] text-secondary fill-1">star</span>
                <span class="font-sans text-sm font-bold text-primary">${c.communityRating || c.userRating}</span>
              </div>
              <span class="text-[9px] text-outline font-semibold mt-0.5">${c.reviewsCount || '340 rev'}</span>
            </div>
          </div>
        </div>

        <!-- Dossier Content Body -->
        <div class="p-3.5 flex flex-col gap-3">

          <!-- 1. Tasted Dossier / Highlights (if Visited) -->
          ${c.tastedItems && c.tastedItems.length > 0 ? `
            <div class="bg-surface-container rounded-xl p-3 flex flex-col gap-2">
              <div class="flex items-center justify-between">
                <div class="flex items-center gap-1.5 text-secondary">
                  <span class="material-symbols-outlined text-[18px]">${isApril ? 'award_star' : 'receipt_long'}</span>
                  <span class="text-xs font-bold uppercase tracking-wider text-on-surface">${isApril ? 'Recent Tasting Highlights' : 'Your Tasted Dossier'}</span>
                </div>
                <span class="text-[11px] text-secondary font-semibold">${isApril ? 'Last: 4 days ago' : `${c.tastedItems.length} logged tastings`}</span>
              </div>
              <div class="space-y-1.5">
                ${c.tastedItems.map(item => `
                  <div class="bg-surface-container-lowest p-2 rounded-lg flex items-start justify-between gap-2 border border-outline-variant/20">
                    <div class="flex flex-col flex-1">
                      <div class="flex items-center gap-1.5 flex-wrap">
                        <span class="text-xs font-semibold text-primary">${item.name}</span>
                        ${item.badge ? `<span class="text-[10px] text-secondary-container px-1.5 py-0.2 rounded bg-primary-container font-semibold">${item.badge}</span>` : ''}
                      </div>
                      <p class="text-[11px] text-on-surface-variant mt-0.5">${item.notes}</p>
                    </div>
                    <div class="flex flex-col items-end flex-shrink-0">
                      <span class="text-xs font-semibold text-on-surface">${item.price}</span>
                      <span class="text-[11px] text-secondary font-bold">★ ${item.rating}</span>
                    </div>
                  </div>
                `).join('')}
              </div>
            </div>
          ` : ''}

          <!-- 2. Wishlist Objectives (if Coffee Collective) -->
          ${c.communityRenowned ? `
            <div class="bg-surface-container rounded-xl p-3 flex flex-col gap-1">
              <div class="flex items-center justify-between">
                <span class="text-xs font-bold uppercase tracking-wider text-on-surface">Community Renowned For</span>
                <span class="text-[11px] text-secondary font-semibold">Direct Trade Pioneer</span>
              </div>
              <p class="text-xs text-on-surface-variant mt-0.5 leading-relaxed">
                Celebrated for single estate Bolivian <span class="font-semibold text-primary">Takesi Geisha</span> and their signature house-made coffee soft serve infused with Kieni espresso.
              </p>
            </div>
          ` : ''}

          <!-- 3. Fetched Menu Sample (e.g. Coffee Collective) -->
          ${c.fetchedMenu && c.fetchedMenu.length > 0 ? `
            <div class="grid grid-cols-3 gap-1.5">
              ${c.fetchedMenu.map(fm => `
                <div class="bg-surface-container-lowest p-2 rounded-lg text-center flex flex-col justify-between border border-outline-variant/20">
                  <span class="text-[11px] font-bold text-primary truncate">${fm.item}</span>
                  <span class="text-xs text-secondary font-semibold mt-1">${fm.price}</span>
                  <span class="text-[9px] text-outline-variant mt-0.5">${fm.tag}</span>
                </div>
              `).join('')}
            </div>
          ` : ''}

          <!-- 4. Digital Live Menu Preview (e.g. Prolog) -->
          ${isProlog && c.liveMenu ? `
            <div class="flex flex-col gap-1.5">
              <div class="flex items-center justify-between">
                <span class="text-[10px] uppercase tracking-wider text-on-surface-variant font-bold">Live Menu Preview</span>
                <button onclick="openLiveMenu('${c.id}')" class="text-[11px] text-secondary font-semibold hover:underline">Full Card (18)</button>
              </div>
              <div class="grid grid-cols-2 gap-1.5">
                <div class="bg-surface-container-high/60 p-2 rounded-lg flex flex-col justify-between">
                  <div class="flex items-center justify-between">
                    <span class="text-xs font-bold text-on-surface">Ethiopia Shantawene</span>
                    <span class="text-[11px] text-secondary font-bold">48 DKK</span>
                  </div>
                  <span class="text-[10px] text-on-surface-variant">Natural • Peach & Jasmine floral notes</span>
                </div>
                <div class="bg-surface-container-high/60 p-2 rounded-lg flex flex-col justify-between">
                  <div class="flex items-center justify-between">
                    <span class="text-xs font-bold text-on-surface">Batch Brew Nordic</span>
                    <span class="text-[11px] text-secondary font-bold">36 DKK</span>
                  </div>
                  <span class="text-[10px] text-on-surface-variant">Colombian Pink Bourbon rotative</span>
                </div>
              </div>
            </div>
          ` : ''}

          <!-- 5. Signature Extraction Bar (e.g. April) -->
          ${c.signatureBar ? `
            <div class="bg-surface-container-high/40 p-2.5 rounded-xl flex items-center justify-between">
              <div class="flex items-center gap-2">
                <span class="material-symbols-outlined text-[18px] text-secondary">tune</span>
                <div class="flex flex-col">
                  <span class="text-xs font-semibold text-on-surface">Signature Extraction Bar</span>
                  <span class="text-[11px] text-on-surface-variant">${c.signatureBar}</span>
                </div>
              </div>
              <span class="text-[10px] font-bold text-secondary uppercase px-2 py-0.5 bg-surface-container rounded-lg">Featured</span>
            </div>
          ` : ''}

          <!-- 6. Action Button Controls -->
          <div class="flex items-center gap-1.5 pt-0.5">
            <button onclick="prefillLoggerWithCafe('${c.id}')" class="flex-1 h-10 ${isVisited ? 'bg-primary-container text-white hover:bg-secondary' : 'bg-secondary text-white hover:bg-primary-container'} rounded-xl text-xs font-semibold flex items-center justify-center gap-1.5 shadow-sm transition-colors active:scale-95">
              <span class="material-symbols-outlined text-[16px]">${isVisited ? (isApril ? 'history_edu' : 'add_circle') : 'bookmark_add'}</span>
              <span>${isVisited ? (isApril ? 'Log Extraction Note' : 'Log New Visit') : 'Saved to Wishlist'}</span>
            </button>
            <button onclick="openDirections('${c.id}')" aria-label="Get Directions" class="w-10 h-10 bg-surface-container rounded-xl text-primary flex items-center justify-center hover:bg-surface-container-high active:scale-95 transition-all" title="Get Directions">
              <span class="material-symbols-outlined text-[18px]">directions</span>
            </button>
            <button onclick="${isVisited ? `shareCafePassport('${c.id}')` : `toggleWishlist('${c.id}')`}" aria-label="${isVisited ? 'Share' : 'Bookmark'}" class="w-10 h-10 bg-surface-container rounded-xl text-primary flex items-center justify-center hover:bg-surface-container-high active:scale-95 transition-all" title="${isVisited ? 'Share' : 'Bookmark'}">
              <span class="material-symbols-outlined text-[18px] ${!isVisited && c.visitedStatus === 'WISHLIST' ? 'text-secondary fill-1' : ''}">${isVisited ? 'share' : (c.visitedStatus === 'WISHLIST' ? 'favorite' : 'bookmark')}</span>
            </button>
          </div>

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
  else if (tabKey === 'cafes') renderCafesScreen();
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

function recenterMap() {
  showToast("GPS recentered on Copenhagen, Vesterbro (within 3 km)");
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
  const name = cafe ? cafe.name : "Cafe";
  showToast(`Navigating to ${name}...`);
  window.open(`https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(name + ' Copenhagen')}`, '_blank');
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
  openModal('modal-log-brew');
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

    const cafeName = document.getElementById('logSelectedCafeName')?.textContent || 'Prolog Coffee Bar';
    const beanName = document.getElementById('logBeanTitle')?.textContent || 'Ethiopia Gesha Village';
    const roaster = document.getElementById('logBeanRoaster')?.textContent?.split('•')[0]?.trim() || 'Onyx Coffee Lab';
    const notesText = document.getElementById('cuppingNotes')?.value || 'Super clean cup, bright citric acidity, delicate floral finish.';
    const selectedTags = Array.from(document.querySelectorAll('#flavorTagsGroup .tag-chip.bg-primary span:first-child')).map(s => s.textContent.trim());

    const newBrew = {
      id: `brew-${Date.now()}`,
      cafeName,
      roaster,
      beanName,
      method: currentLogMethod || 'Pour Over',
      methodBadge: currentLogMethod || 'Pour Over',
      ratio: 'Diego Bermudez Lot #42 • 15g / 240g',
      price: '55 DKK',
      temperature: currentLogTemp,
      vessel: currentLogVessel,
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
      showToast("Saved Entry #142 to Cafe Passport! ☕");
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
  selectTab('journal');
});
