package com.near_reality.cache_tool.packing.custom

import com.near_reality.api.model.Bond
import com.near_reality.api.model.CreditStoreCategory
import com.zenyte.game.item.ids.*
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import mgi.tools.parser.TypeParser
import mgi.types.config.ParamDefinitions
import mgi.types.config.StructDefinitions
import mgi.types.config.enums.EnumDefinitions
import net.runelite.cache.util.ScriptVarType
import java.io.File

object NearRealityStoreInterfacePacker {

	const val CATEGORY_ENUM = 9900
	const val LOYALTY_DONATED_ENUM = 9901
	const val LOYALTY_REWARDS_ENUM = 9902
	const val BENEFITS_ENUM = 9903
	const val BENEFITS_RANKS_ENUM = 9904
	const val OFFERS_ENUM = 9905
	const val LOYALTY_REWARDS_COUNT_ENUM = 9906
	const val LOYALTY_CATEGORY_ENUM = 9907

	const val BENEFITS_BENEFIT_DESC_PARAM = 5100
	const val BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM = 5101
	const val BENEFITS_BENEFIT_TOPAZ_PARAM = 5102
	const val BENEFITS_BENEFIT_SAPPHIRE_PARAM = 5103
	const val BENEFITS_BENEFIT_EMERALD_PARAM = 5104
	const val BENEFITS_BENEFIT_RUBY_PARAM = 5105
	const val BENEFITS_BENEFIT_DIAMOND_PARAM = 5106
	const val BENEFITS_BENEFIT_DRAGONSTONE_PARAM = 5107
	const val BENEFITS_BENEFIT_ONYX_PARAM = 5108
	const val BENEFITS_BENEFIT_ZENYTE_PARAM = 5109
	const val BENEFITS_BENEFIT_ENCHANTED_PARAM = 5110
	const val BENEFITS_BENEFIT_GOLD_PARAM = 5111
	const val BENEFITS_BENEFIT_ETERNAL_PARAM = 5112
	const val BENEFITS_BENEFIT_NEBULA_PARAM = 5113
	const val BENEFITS_BENEFIT_CATALYTIC_PARAM = 5114

	const val OFFERS_NAME_PARAM = 10000
	const val OFFERS_BONUS_PARAM = 10001
	const val OFFERS_PRICE_PARAM = 10002
	const val OFFERS_GRAPHIC_PARAM = 10003
	const val OFFERS_GRAPHIC_WIDTH_PARAM = 5115
	const val OFFERS_GRAPHIC_HEIGHT_PARAM = 5116
	const val OFFERS_BOND_CONVERT_PARAM = 5117

	@JvmStatic
	fun pack() {
		TypeParser.packClientScriptsRecursive("assets/osnr/store_interface_new/cs2")
		packIf3()
		packEnums()
		packParams()
		packStructs()
	}

	private fun packIf3() {
		TypeParser.packInterface(File("assets/osnr/store_interface_new/if3/"), 1622)
	}

	private fun packEnums() {
		EnumDefinitions(LOYALTY_CATEGORY_ENUM, 'i', 's').apply {
			var idx = 0
			this.values[idx] = "Bundles"
			this.pack()
		}
		EnumDefinitions(CATEGORY_ENUM, 'i', 's').apply {
			var idx = 0
			for (entry in CreditStoreCategory.entries) {
				this.values[idx++] = entry.getDisplayName()
			}
			this.pack()
		}
		EnumDefinitions(LOYALTY_DONATED_ENUM, 'i', 'i').apply {
			defaultInt = Int.MAX_VALUE
			var idx = 0
			this.values[idx++] = 0
			this.values[idx++] = 25
			this.values[idx++] = 50
			this.values[idx++] = 100
			this.values[idx++] = 250
			this.values[idx++] = 500
			this.values[idx++] = 1000
			this.values[idx++] = 1500
			this.values[idx++] = 2500
			this.values[idx] = 5000
			this.pack()
		}
		EnumDefinitions(LOYALTY_REWARDS_ENUM, 'i', 'i').apply {
			var idx = 0
			this.values[idx++] = -1
			this.values[idx++] = MYSTERY_BOX
			this.values[idx++] = SUPER_MYSTERY_BOX
			this.values[idx++] = ULTIMATE_MYSTERY_BOX
			this.values[idx++] = REGAL_MYSTERY_BOX
			this.values[idx++] = CUSTOM_ITEM_TOKEN
			this.values[idx++] = REGAL_MYSTERY_BOX
			this.values[idx++] = CUSTOM_ITEM_SET_TOKEN
			this.values[idx++] = CUSTOM_ITEM_SET_TOKEN
			this.values[idx] = NEAR_REALITY_PARTY_HAT
			this.pack()
		}
		EnumDefinitions(LOYALTY_REWARDS_COUNT_ENUM, 'i', 'i').apply {
			var idx = 0
			this.values[idx++] = -1
			this.values[idx++] = 1
			this.values[idx++] = 1
			this.values[idx++] = 1
			this.values[idx++] = 2
			this.values[idx++] = 1
			this.values[idx++] = 5
			this.values[idx++] = 1
			this.values[idx++] = 3
			this.values[idx] = 1
			this.pack()
		}
		EnumDefinitions(BENEFITS_RANKS_ENUM, 'i', 'i').apply {
			var idx = 0
			this.values[idx++] = BENEFITS_BENEFIT_TOPAZ_PARAM
			this.values[idx++] = BENEFITS_BENEFIT_SAPPHIRE_PARAM
			this.values[idx++] = BENEFITS_BENEFIT_EMERALD_PARAM
			this.values[idx++] = BENEFITS_BENEFIT_RUBY_PARAM
			this.values[idx++] = BENEFITS_BENEFIT_DIAMOND_PARAM
			this.values[idx++] = BENEFITS_BENEFIT_DRAGONSTONE_PARAM
			this.values[idx++] = BENEFITS_BENEFIT_ONYX_PARAM
			this.values[idx++] = BENEFITS_BENEFIT_ZENYTE_PARAM
			this.values[idx++] = BENEFITS_BENEFIT_ENCHANTED_PARAM
			this.values[idx++] = BENEFITS_BENEFIT_GOLD_PARAM
			this.values[idx++] = BENEFITS_BENEFIT_ETERNAL_PARAM
			this.values[idx++] = BENEFITS_BENEFIT_NEBULA_PARAM
			this.values[idx] = BENEFITS_BENEFIT_CATALYTIC_PARAM
			this.pack()
		}
	}

	private fun packParams() {
		ParamDefinitions(BENEFITS_BENEFIT_DESC_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM, ScriptVarType.INTEGER).pack()
		ParamDefinitions(BENEFITS_BENEFIT_TOPAZ_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(BENEFITS_BENEFIT_SAPPHIRE_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(BENEFITS_BENEFIT_EMERALD_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(BENEFITS_BENEFIT_RUBY_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(BENEFITS_BENEFIT_DIAMOND_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(BENEFITS_BENEFIT_DRAGONSTONE_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(BENEFITS_BENEFIT_ONYX_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(BENEFITS_BENEFIT_ZENYTE_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(BENEFITS_BENEFIT_ENCHANTED_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(BENEFITS_BENEFIT_GOLD_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(BENEFITS_BENEFIT_ETERNAL_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(BENEFITS_BENEFIT_NEBULA_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(BENEFITS_BENEFIT_CATALYTIC_PARAM, ScriptVarType.STRING).pack()

		ParamDefinitions(OFFERS_NAME_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(OFFERS_BONUS_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(OFFERS_PRICE_PARAM, ScriptVarType.STRING).pack()
		ParamDefinitions(OFFERS_GRAPHIC_PARAM, ScriptVarType.INTEGER).pack()
		ParamDefinitions(OFFERS_GRAPHIC_WIDTH_PARAM, ScriptVarType.INTEGER).pack()
		ParamDefinitions(OFFERS_GRAPHIC_HEIGHT_PARAM, ScriptVarType.INTEGER).pack()
		ParamDefinitions(OFFERS_BOND_CONVERT_PARAM, ScriptVarType.INTEGER).pack()
	}

	private fun packStructs() {
		EnumDefinitions(OFFERS_ENUM, 'i', 'i').apply {
			defaultInt = -1
			var idx = 0

			StructDefinitions(12000).apply {
				parameters = Int2ObjectOpenHashMap(8)
				parameters[OFFERS_NAME_PARAM] = "$5 DPin"
				parameters[OFFERS_BONUS_PARAM] = ""
				parameters[OFFERS_PRICE_PARAM] = "$4.99"
				parameters[OFFERS_GRAPHIC_PARAM] = 5259
				parameters[OFFERS_GRAPHIC_WIDTH_PARAM] = 40
				parameters[OFFERS_GRAPHIC_HEIGHT_PARAM] = 32
				parameters[OFFERS_BOND_CONVERT_PARAM] = Bond.DONATOR_PIN_5.credits
				values[idx++] = id
				pack()
			}

			StructDefinitions(12001).apply {
				parameters = Int2ObjectOpenHashMap(8)
				parameters[OFFERS_NAME_PARAM] = "$10 DPin"
				parameters[OFFERS_BONUS_PARAM] = "+10 bonus credits"
				parameters[OFFERS_PRICE_PARAM] = "$9.99"
				parameters[OFFERS_GRAPHIC_PARAM] = 5260
				parameters[OFFERS_GRAPHIC_WIDTH_PARAM] = 67
				parameters[OFFERS_GRAPHIC_HEIGHT_PARAM] = 45
				parameters[OFFERS_BOND_CONVERT_PARAM] = Bond.DONATOR_PIN_10.credits
				values[idx++] = id
				pack()
			}

			StructDefinitions(12002).apply {
				parameters = Int2ObjectOpenHashMap(8)
				parameters[OFFERS_NAME_PARAM] = "$25 DPin"
				parameters[OFFERS_BONUS_PARAM] = "+25 bonus credits"
				parameters[OFFERS_PRICE_PARAM] = "$24.99"
				parameters[OFFERS_GRAPHIC_PARAM] = 5261
				parameters[OFFERS_GRAPHIC_WIDTH_PARAM] = 67
				parameters[OFFERS_GRAPHIC_HEIGHT_PARAM] = 57
				parameters[OFFERS_BOND_CONVERT_PARAM] = Bond.DONATOR_PIN_25.credits
				values[idx++] = id
				pack()
			}

			StructDefinitions(12003).apply {
				parameters = Int2ObjectOpenHashMap(8)
				parameters[OFFERS_NAME_PARAM] = "$35 DPin"
				parameters[OFFERS_BONUS_PARAM] = "+35 bonus credits"
				parameters[OFFERS_PRICE_PARAM] = "$34.99"
				parameters[OFFERS_GRAPHIC_PARAM] = 5262
				parameters[OFFERS_GRAPHIC_WIDTH_PARAM] = 51
				parameters[OFFERS_GRAPHIC_HEIGHT_PARAM] = 44
				parameters[OFFERS_BOND_CONVERT_PARAM] = Bond.DONATOR_PIN_35.credits
				values[idx++] = id
				pack()
			}

			StructDefinitions(12004).apply {
				parameters = Int2ObjectOpenHashMap(8)
				parameters[OFFERS_NAME_PARAM] = "$50 DPin"
				parameters[OFFERS_BONUS_PARAM] = "+70 bonus credits"
				parameters[OFFERS_PRICE_PARAM] = "$49.99"
				parameters[OFFERS_GRAPHIC_PARAM] = 5263
				parameters[OFFERS_GRAPHIC_WIDTH_PARAM] = 77
				parameters[OFFERS_GRAPHIC_HEIGHT_PARAM] = 44
				parameters[OFFERS_BOND_CONVERT_PARAM] = Bond.DONATOR_PIN_50.credits
				values[idx++] = id
				pack()
			}

			StructDefinitions(12005).apply {
				parameters = Int2ObjectOpenHashMap(8)
				parameters[OFFERS_NAME_PARAM] = "$100 DPin"
				parameters[OFFERS_BONUS_PARAM] = "+200 bonus credits"
				parameters[OFFERS_PRICE_PARAM] = "$99.99"
				parameters[OFFERS_GRAPHIC_PARAM] = 5264
				parameters[OFFERS_GRAPHIC_WIDTH_PARAM] = 101
				parameters[OFFERS_GRAPHIC_HEIGHT_PARAM] = 54
				parameters[OFFERS_BOND_CONVERT_PARAM] = Bond.DONATOR_PIN_100.credits
				values[idx++] = id
				pack()
			}

			pack()
		}

		EnumDefinitions(BENEFITS_ENUM, 'i', 'i').apply {
			defaultInt = -1
			var idx = 0

			//Killstreaks.java
			StructDefinitions(10600).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Blood Money Boost on Kills"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "5%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "8%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "12%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "17%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "21%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "70%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "125%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "150%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "200%"
				values[idx++] = id
				this.pack()
			}

			//MemberRank.java
			StructDefinitions(10601).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Drop Rate Boost on uniques and pets"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "2%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "3%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "4%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "5%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "6%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "8%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "12%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "14%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "16%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "18%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "20%"
				values[idx++] = id
				this.pack()
			}

			//PresetManager.java
			StructDefinitions(10602).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Extra Preset slots"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "3"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "3"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "5"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "7"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "7"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "9"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "11"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "13"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "15"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "15"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "15"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "15"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "15"
				values[idx++] = id
				this.pack()
			}

			//PrayerManager.java
			StructDefinitions(10603).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Prayer Drain resistance (outside of wilderness)"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1.5x"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "2x"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "2.5x"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "3x"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "3.5x"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "4x"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "4.5x"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "5x"
				values[idx++] = id
				this.pack()
			}

			//HomeRejuvanationPool.java
			StructDefinitions(10604).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Rejuvenation pool Special attack restore in seconds"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "120"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "90"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "60"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "30"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "0"
				values[idx++] = id
				this.pack()
			}

			//?
			StructDefinitions(10605).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] =
					"Special Recharge (% special recharge upon killing a player in Edgeville)"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "60%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "60%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "100%"
				values[idx++] = id
				this.pack()
			}

			//CoalBag.java
			StructDefinitions(10606).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Coal Bag Capacity"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "56"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "56"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "100"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "250"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "250"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "350"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "500"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "600"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "750"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "750"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "750"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "750"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "750"
				values[idx++] = id
				this.pack()
			}

			//?
			StructDefinitions(10607).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Bank Capacity (Total)"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "900"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "1000"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "1100"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1220"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1220"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1220"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1220"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1220"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1220"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1220"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1220"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1220"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1220"
				values[idx++] = id
				this.pack()
			}

			//MemberRank
			StructDefinitions(10608).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Yell Cooldown (in seconds)"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "60"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "50"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "40"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "30"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "20"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "10"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "0"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10609).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Godwars Chamber Killcount Requirement"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "10"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "8"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "6"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "4"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "0"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10610).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Barrows Repair Discount"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "35%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "40%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "45%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "50%"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10611).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Extra Chance to Spawn Superior Slayer Monster"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "5%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "8%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "12%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "15%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "18%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "40%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "60%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "75%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "100%"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10612).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Extra Chance to Roll Clue Bottle, Nest, Geode"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "5%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "8%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "12%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "15%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "18%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "40%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "60%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "75%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "100%"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10613).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Extra Chance to Spawn an Additional Mark of Grace"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "5%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "8%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "12%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "15%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "18%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "40%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "60%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "75%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "100%"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10614).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Additional Crystal Key Loot Rolls"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "3"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10615).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Additional Enhanced Crystal key Loot Rolls"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "3"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10616).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Discount on Zahur Services"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "3%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "3%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "5%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "13%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "15%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "25%"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10617).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Extra Chance to Save a Steel Bar When Smithing Cannonballs"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "5%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "8%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "12%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "15%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "35%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "40%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "60%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "70%"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10618).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Extra Yield When Harvesting Plants"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "15%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "35%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "40%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "60%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "70%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "80%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "90%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "100%"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10619).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Extra Bonus PKP"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "35%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "40%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "45%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "50%"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10620).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Extra Chance to Receive an Additional Voting Point"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "15%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "15%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "40%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "60%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "70%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "80%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "100%"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10621).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Extra Pest Control Points Per Game"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "2"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "2"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10622).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Extra Time in Tears of Guthix Minigame (in seconds)"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "10"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "10"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "20"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "20"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "30"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "40"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "50"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "60"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "60"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "60"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10623).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Discount on Points Lost in Raid When Dying"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "5%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "5%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "15%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "35%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "40%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "40%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "40%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "60%"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10624).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Birdhouse Timer (in minutes)"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "35"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "35"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "30"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "30"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "25"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "20"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "20"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "20"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "20"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "15"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "15"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "15"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "10"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10625).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Slayer Point Cost When Cancelling Task"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "25"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "25"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "20"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "20"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "20"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "20"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "18"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "14"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "10"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "5"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "5"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "0"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10626).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Additional battlestaves from zaff in varrock (per day)"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "30"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "60"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "60"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "75"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "90"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "100"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "120"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "120"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "120"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "120"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "120"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10627).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] =
					"Chance to receive noted resources (Mining, Woodcutting, Fishing)"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "15%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "15%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "15%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "40%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "60%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "75%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "100%"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10628).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Extra Chance to Find a Pet"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "1%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "2%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "3%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "6%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "6%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "8%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "13%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "15%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "50%"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10629).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Chance to catch an extra chinchompa from box traps"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "20%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "40%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "60%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "75%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "100%"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10630).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Chance to receive extra stardust when mining crashed stars"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "5%"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "10%"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "15%"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "25%"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "30%"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "40%"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "50%"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "75%"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "100%"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "100%"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10631).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Thralls last longer"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "10s"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "10s"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "15s"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "15s"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "20s"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "20s"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "25s"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "25s"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "30s"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "40s"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "50s"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1min"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1min"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10632).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Ability to set a custom yell-tag"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "100m"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "50m"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "Free"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "Free"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "Free"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "Free"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10633).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Ability to set a custom loyalty title"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 1
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "-"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "100m"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "50m"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "Free"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "Free"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "Free"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "Free"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10634).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Donator Island"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10635).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Smithing: Ability to use noted coal when smelting"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10636).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Noted Drops: Bars dropped from metal dragons"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10637).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] =
					"Blood money will automatically be sent to your inventory upon each kill"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10638).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Obelisks (set destination)"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10639).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Underground Portion of Shilo Village Gem Rock Mine"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10640).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Bird Nests Sent to Inventory (with space)"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10641).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] =
					"Bird and clue nests dropped from woodcutting go straight into your inventory"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10642).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Auto-pickup blood money"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10643).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] =
					"The dwarf-multicannon will automatically reload cannonballs from your inventory"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10644).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] =
					"Auto-cut fish obtained from aerial fishing (toggle by speaking to alry the angler)"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10645).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Hint icon for determining the Giant Mole's current location"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10646).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Weeds do not grow in farming patches"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10647).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Free access to the second floor of Motherlode Mine"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10648).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Receive noted bars at blast furnace"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10649).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Private Red Chinchompa Hunting Ground"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10650).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Free Protection in Karuulm Slayer Dungeon without boots"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10651).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] =
					"50% chance of receiving an extra nest when harvesting birdhouses"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10652).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Reduced steps on medium-master clues"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			StructDefinitions(10653).apply {
				parameters = Int2ObjectOpenHashMap(16)
				parameters[BENEFITS_BENEFIT_DESC_PARAM] = "Private Black Chinchompa Hunting Ground"
				parameters[BENEFITS_BENEFIT_TYPE_IS_STRING_PARAM] = 0
				parameters[BENEFITS_BENEFIT_TOPAZ_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_SAPPHIRE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_EMERALD_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_RUBY_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_DIAMOND_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_DRAGONSTONE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ONYX_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ZENYTE_PARAM] = "0"
				parameters[BENEFITS_BENEFIT_ENCHANTED_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_GOLD_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_ETERNAL_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_NEBULA_PARAM] = "1"
				parameters[BENEFITS_BENEFIT_CATALYTIC_PARAM] = "1"
				values[idx++] = id
				this.pack()
			}

			pack()
		}
	}

}